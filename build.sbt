<<<<<<< HEAD
name := "figure_userprofile_streaming"
=======
name := "kafka-streaming-consumer"
>>>>>>> 5137abd781db77df504212266cde5f858d65810d

version := "1.0"

scalaVersion := "2.10.5"

<<<<<<< HEAD
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.6.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.0"
=======
libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.4.0"

libraryDependencies += "org.apache.spark" % "spark-sql_2.10" % "1.4.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.4.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.4.0"

libraryDependencies += "org.apache.spark" % "spark-mllib_2.10" % "1.4.0"

libraryDependencies += "org.apache.spark" % "spark-graphx_2.10" % "1.4.0"

libraryDependencies += "org.mongodb.mongo-hadoop" % "mongo-hadoop-core" % "1.4.1"
>>>>>>> 5137abd781db77df504212266cde5f858d65810d

libraryDependencies += "org.mongodb" % "bson" % "3.0.4"

libraryDependencies += "org.mongodb" % "mongo-java-driver" % "3.0.4"

<<<<<<< HEAD
//libraryDependencies += "redis.clients" % "jedis" % "2.7.2"

//libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1"

libraryDependencies += "org.json" % "json" % "20141113"

libraryDependencies += "redis.clients" % "jedis" % "2.7.2"

=======
libraryDependencies += "redis.clients" % "jedis" % "2.7.2"

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1"

libraryDependencies += "org.json" % "json" % "20141113"

>>>>>>> 5137abd781db77df504212266cde5f858d65810d
libraryDependencies += "com.google.code.gson" % "gson" % "2.3"

libraryDependencies += "com.alibaba" % "fastjson" % "1.2.7"

resolvers += "Akka Repository" at "http://repo.akka.io/releases/"