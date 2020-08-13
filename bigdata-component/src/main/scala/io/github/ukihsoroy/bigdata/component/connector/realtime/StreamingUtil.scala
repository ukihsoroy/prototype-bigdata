package io.github.ukihsoroy.bigdata.component.connector.realtime

import java.util.Properties

import io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.hbase.HBaseSink
import io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.kafka.KafkaSink
import io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.mysql.MysqlSink
import io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.redis.RedisSink
import org.apache.kafka.common.serialization.StringSerializer
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}

object StreamingUtil {

  def getKafkaProducerBroadcast(sc: SparkContext, zkHost: String): Broadcast[KafkaSink[String, String]] = {
    val kafkaProducer: Broadcast[KafkaSink[String, String]] = {
      val kafkaProducerConfig = {
        val p = new Properties()
        p.setProperty("bootstrap.servers", zkHost)
        p.put("key.serializer", classOf[StringSerializer])
        p.put("value.serializer", classOf[StringSerializer])
        p
      }
      sc.broadcast(KafkaSink[String, String](kafkaProducerConfig))
    }
    kafkaProducer
  }

  def getHBaseConnBroadcast(sc: SparkContext, zkHost: String, confFile: String): Broadcast[HBaseSink] = {
    sc.broadcast(HBaseSink(zkHost, confFile))
  }

  def getMySqlConnBroadcast(sc: SparkContext, host: String, port: String, db: String, user: String, pwd: String): Broadcast[MysqlSink] = {
    sc.broadcast(MysqlSink(host, port, db, user, pwd))
  }

  def getMySqlConnBroadcast(sc: SparkContext): Broadcast[MysqlSink] = {
    val conf = sc.getConf
    if (checkConf(conf, "mysql.host") && checkConf(conf, "mysql.port")
      && checkConf(conf, "mysql.db") && checkConf(conf, "mysql.username")
      && checkConf(conf, "mysql.password")) {
      val host = sc.getConf.get("mysql.host")
      val port = sc.getConf.get("mysql.port")
      val db = sc.getConf.get("mysql.db")
      val user = sc.getConf.get("mysql.username")
      val pwd = sc.getConf.get("mysql.password")
      getMySqlConnBroadcast(sc, host, port, db, user, pwd)
    }
    else {
      getMySqlConnBroadcast(sc, "", "", "", "", "")
    }
  }

  def getRedisPoolBroadcast(sc: SparkContext, masterName: String, hostAndPort: String): Broadcast[RedisSink] = {
    sc.broadcast(RedisSink(masterName, hostAndPort))
  }

  def getRedisPoolBroadcast(sc: SparkContext): Broadcast[RedisSink] = {
    val conf = sc.getConf
    if (checkConf(conf, "redis.masterName") && checkConf(conf, "redis.hostAndPort")) {
      val masterName = conf.get("redis.masterName")
      val hostAndPort = conf.get("redis.hostAndPort")
      getRedisPoolBroadcast(sc, masterName, hostAndPort)
    }
    else {
      getRedisPoolBroadcast(sc, "", "")
    }
  }


  def getHBaseConnBroadcast(sc: SparkContext): Broadcast[HBaseSink] = {
    val conf = sc.getConf
    if (checkConf(conf, "hbase.host") && checkConf(conf, "hbase.config")) {
      val zkHost = conf.get("hbase.host")
      val confFile = conf.get("hbase.config")
      getHBaseConnBroadcast(sc, zkHost, confFile)
    } else {
      getHBaseConnBroadcast(sc, "", "")
    }
  }

  def getKafkaProducerBroadcast(sc: SparkContext): Broadcast[KafkaSink[String, String]] = {
    val conf = sc.getConf
    if (checkConf(conf, "bootstrap.servers")) {
      val zkHost = sc.getConf.get("bootstrap.servers")
      getKafkaProducerBroadcast(sc, zkHost)
    } else {
      getKafkaProducerBroadcast(sc, "")
    }
  }


  private def checkConf(conf: SparkConf, key: String) = {
    conf.contains(key) && conf.get(key) != "" && conf.get(key) != null
  }


}
