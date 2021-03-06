package io.github.ukihsoroy.bigdata.framework.kafka.impl

import java.util.Properties

import io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.kafka.KafkaSink
import io.github.ukihsoroy.bigdata.framework.EmrComputable
import io.github.ukihsoroy.bigdata.framework.kafka.TKafkaRepository
import io.github.ukihsoroy.bigdata.framework.spark.Sparkle
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010.ConsumerStrategies._
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies._

trait SparkKafkaRepositoryImpl[B] extends TKafkaRepository[Sparkle, DStream[B]] {

  protected def transJson2Bean(jsonStream: DStream[String]): DStream[B]

  protected def transBean2Json(beanStream: DStream[B]): DStream[String]

  override def readStream(implicit env: Sparkle): DStream[B] = {
    val bootstrapServers = env.getParameter("kafka.bootstrap.servers")
    val groupId = env.getParameter("kafka.group.id")
    val topic = env.getParameter("kafka.topic")
    val kafkaParams: Map[String, Object] = Map[String, Object](
      "bootstrap.servers" -> BOOTSTRAP_SERVERS,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> GROUP_ID,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (true: java.lang.Boolean)
    )

    println("kafka params:")
    kafkaParams.foreach(k => println(s"${k._1}:${k._2}"))
    println(s"topic:$TOPIC")

    val jsonStream = KafkaUtils.createDirectStream[String, String](
      env.ssc,
      PreferConsistent,
      Subscribe[String, String](TOPIC.split(","), kafkaParams)
    ).map(_.value)
    transJson2Bean(jsonStream)
  }

  override def writeStream(result: DStream[B])
                          (implicit env: Sparkle): Unit = {

    println("init kafka producer")

    val kafkaProducer: Broadcast[KafkaSink[String, String]] = {
      val kafkaProducerConfig = {
        val p = new Properties()
        p.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS)
        p.put("key.serializer", classOf[StringSerializer])
        p.put("value.serializer", classOf[StringSerializer])
        p
      }
      env.sc.broadcast(KafkaSink[String, String](kafkaProducerConfig))
    }

    println(s"write to : $BOOTSTRAP_SERVERS, topic: $TOPIC")

    val stream = transBean2Json(result)
    stream.foreachRDD {
      rdd =>
        if (!rdd.isEmpty()) {
          rdd.foreach {
            r =>
              kafkaProducer.value.send(TOPIC, r)
          }
        }
    }
  }
}
