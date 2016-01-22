#!/bin/bash
source ~/.bashrc

SPARH_HOME=/home/hadoop/caishi/local/spark
/opt/work/spark-1.4.0-bin-hadoop2.6/bin/spark-submit \
    --master spark://127.0.0.1:7077 \
    --num-executors 2 \
    --driver-memory 4g \
    --executor-memory 2g \
    --executor-cores 2 \
    ./kafka-streaming-consumer.jar \
        10.4.1.221:2181,10.4.1.222:2181,10.4.1.223:2181 \
        10.4.1.201:9092,10.4.1.202:9092,10.4.1.203:9092 \
        topic_comment_event,topic_news_behavior,topic_news_social,topic_common_event,topic_scene_behavior \
        30 \
        2 \
        hdfs://10.4.1.4:9000/test/dw
