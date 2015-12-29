package com.caishi.util

import java.util

import com.mongodb.{ServerAddress, MongoClient}
import com.mongodb.client.model.{UpdateOptions, Filters}
import com.mongodb.client.result.UpdateResult
import org.bson.Document

/**
 * mongodb 辅助类
 * Created by root on 15-10-17.
 */
object MongoUtil {
  @transient private var instance : MongoClient = _

  def getInstance(address : String) : MongoClient = {
    if(instance == null){
      val adds : Array[ServerAddress] = address.split(",").map(rem => new ServerAddress(rem.split(":")(0),rem.split(":")(1).toInt))
      val t : util.ArrayList[ServerAddress] = new util.ArrayList[ServerAddress]()
      adds.foreach(s => t.add(s))

      instance = new MongoClient(t)
    }
    instance
  }

  /**
   * 更新or添加
   */
  def upsert(mongoRemotes:String,mongoDb:String,collection:String,userId:String,doc: Document):Unit ={
    val result : UpdateResult =MongoUtil.getInstance(mongoRemotes).getDatabase(mongoDb).getCollection(collection).updateMany(Filters.eq("_id", userId), new Document("$set", doc), new UpdateOptions().upsert(true))
    result.getModifiedCount
  }

  /**
   * 根据_ID获得一个doc对象
   * @param mongoRemotes
   * @param mongoDb
   * @param collection
   * @param userId
   * @return
   */
  def getFirstDoc(mongoRemotes:String,mongoDb:String,collection:String,userId:String):Document ={
    var doc: Document = MongoUtil.getInstance(mongoRemotes).getDatabase(mongoDb).getCollection(collection).find(Filters.eq("_id", userId)).first()
    if(doc == null){// 如果为空则默认使用default用户的画像作为基础
      doc = MongoUtil.getInstance(mongoRemotes).getDatabase(mongoDb).getCollection(collection).find(Filters.eq("_id", "default")).first()
      doc = doc.append("_id",userId)
    }
    doc
  }
}
