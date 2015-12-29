package com.caishi.kafka.customer.figure

import com.caishi.model.Util
import org.apache.commons.lang3.StringUtils
import org.bson.Document
import _root_.util.JsonUtil
import com.caishi.util.MongoUtil
import kafka.serializer.StringDecoder
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{SparkConf}

import scala.util.parsing.json.JSON

/**
 *计算用户画像:冷启动选项和dislike
  1. 接收到的topic根据用户分组
  2. 从mongo中查询出每个用户已有的userprofiles
  3.将接收到的每个用户的分类选择格式化
  4.拟合数据
  5.upsert到mongo中
 * Created by root on 15-11-20.
 */
object FigureStreaming {
  def main(args: Array[String]): Unit = {
    if (args.length < 9) {
      System.err.println("Usage: Common <zkQuorum> <brokers> <topics> <timeWindow> <numRepartition> <pathPre:hdfs pre >")
      System.exit(1)
    }
//    offline zk: 10.10.42.24:2181,10.10.42.25:2128,10.10.42.24:2128
    val Array(brokers, topics, timeWindow, numRepartition,mongoRemotes, mongoDb,collection,dislikeBase,dislikeExponent,topN) = args
//    val zkQuorum:String = "10.4.1.221:2181,10.4.1.222:2181,10.4.1.223:2181"
//    val zkQuorum:String = "10.10.42.24:2181,10.10.42.25:2128,10.10.42.24:2128"

//    val brokers : String = "10.4.1.201:9092,10.4.1.202:9092,10.4.1.203:9092"
//    val topics : String = "topic_comment_event,topic_news_behavior,topic_news_social,topic_common_event,topic_scene_behavior"
//    val brokers : String = "10.10.42.24:9092,10.10.42.25:9092,10.10.42.26:9092"
//    val topics : String = "topic_personalization_event,topic_news_social"
//    val timeWindow : Int = 30
//    val numRepartition : Int = 1
//    val mongoRemotes="10.1.1.134:27017"
//    val mongoDb = "user_profiles"
//    val collection ="usercatLike"
//    val dislikeBase = 0.2
//    val dislikeExponent = 0.4
    val sparkConf = new SparkConf().setAppName("spark-streaming-figure")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition","10000")
    val ssc = new StreamingContext(sparkConf, Seconds(timeWindow.toInt))

//    ssc.sparkContext.setLocalProperty("spark.scheduler.pool","production")
    // Kafka configurations
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder",
      "group.id" -> "spark_streaming_figure",
      "auto.offset.reset" -> "smallest"
      )

    // Since Spark 1.3 we can use Direct Stream
    val topicsSet = topics.split(",").toSet
    val km = new KafkaManager(kafkaParams)
    //val data = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    val data = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,topicsSet)

    val lines = data.repartition(numRepartition.toInt).map(_._2).filter(StringUtils.isNotEmpty(_))
    // 冷启动用户选择分类计算
    lines.filter(checkDataType(_,"topic_personalization_event")).map(json =>{
        val m = JsonUtil.jsonToMap2(json,"fondIds")
        (m.keySet().iterator().next(),m.values().iterator().next())
    }).reduceByKey(_+","+_).foreachRDD(rdd=>{
      rdd.foreachPartition(tup =>{
          tup.foreach(kv=>{
            val userId:String = kv._1
            val m2 :String = kv._2
            try {
              // 获得原有mongodb中用户的画像对象
              var doc: Document = MongoUtil.getFirstDoc(mongoRemotes,mongoDb,collection,userId)
              Util.updateDoc(m2,doc,topN.toInt)
              //upsert 用户画像
              MongoUtil.upsert(mongoRemotes,mongoDb,collection,userId,doc)
            }catch {
              case e:Exception => println("＃＃＃＃＃＃＃＃＃＃＃topic_personalization_event更新ｍｏｎｇｏｄｂ异常！＃＃＃＃＃＃＃＃＃＃＃＃")
            }
          })
      })
    })

    // dislike 计算
    lines.filter(checkDataType(_,"topic_news_social")).map(json =>{
      val m = JsonUtil.jsonToMapForDisLike(json,"categoryIds")
      (m.keySet().iterator().next(),m.values().iterator().next())
    }).reduceByKey(_+","+_).foreachRDD(rdd=>{
      rdd.foreachPartition(tup =>{
        tup.foreach(kv=>{
          val userId:String = kv._1
          val m2 :String = kv._2
          try {
            // 获得原有mongodb中用户的画像对象
            var doc: Document = MongoUtil.getFirstDoc(mongoRemotes,mongoDb,collection,userId)
            Util.updateDisLikeDoc(m2,doc,dislikeBase.toDouble,dislikeExponent.toDouble)
            //upsert 用户画像
            MongoUtil.upsert(mongoRemotes,mongoDb,collection,userId,doc)
          }catch {
            case e:Exception => println("＃＃＃＃＃＃＃＃＃＃＃topic_news_social更新ｍｏｎｇｏｄｂ异常！＃＃＃＃＃＃＃＃＃＃＃＃")
          }
        })
      })
    })

//    更新kafka 监控offset值
    data.foreachRDD(rdd => {
      if(!rdd.isEmpty()){
        //更新zk offset
        km.updateZKOffsets(rdd)
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }


  def checkDataType(d : String,dataType:String): Boolean ={
    val s = JSON.parseFull(d)
    val isType:Boolean = s match {
      case Some(map: Map[String, Any]) =>{
        try {
          dataType.equals(map("topic"))
        }catch {
          case e => false
        }
      }
    }
    isType
  }
}
