package io.github.ukihsoroy.bigdata.framework.spark

import java.util.{Properties, ResourceBundle}

import io.github.ukihsoroy.bigdata.framework.EmrComputable
import io.github.ukihsoroy.bigdata.framework.enums.{EnvType, SparkType}
import jodd.util.StringPool
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable

/**
 * description: Sparkle <br>
 * date: 7/20/2020 21:29 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
class Sparkle(args: Array[String]) extends EmrComputable {

  var conf: SparkConf = _

  var session: SparkSession = _

  var sc: SparkContext = _

  var ssc: StreamingContext = _

  {
    emrParams = buildEmrParams(args)

    val env = getParameter("env", "dev")
    envParams = buildEnvParams(env)

    conf = new SparkConf()

    if (EnvType.DEV.toString.equals(env)) {
      conf.setAppName(getParameter("app.name")).setMaster("local[2]")
    } else {
      conf.setAppName(getParameter("app.name"))
    }

    val sparkType = getParameter("spark.type", SparkType.SQL.toString)
    if (SparkType.SQL.toString.equals(sparkType)) {
      session = SparkSession.builder().config(conf).getOrCreate()
      sc = session.sparkContext
    } else if (SparkType.STREAM.toString.equals(sparkType)) {
      val seconds = emrParams.getOrElse("seconds", "5").toInt
      ssc = new StreamingContext(conf, Seconds(seconds))
      sc = ssc.sparkContext
    } else {
      println("other...")
    }
  }

  override protected def buildEnvParams(env: String): Map[String, String] = {
    val resource = ResourceBundle.getBundle(s"$env/application")
    val keys = resource.keySet().iterator()
    val envMap = mutable.Map[String, String]()
    while (keys.hasNext) {
      val key = keys.next()
      val value = resource.getString(key)
      envMap.put(key, value)
    }
    envMap.toMap
  }

  /**
   * 构建Emr参数
   * @param args
   * @return
   */
  override protected def buildEmrParams(args: Array[String]): Map[String, String] = {
    (
      for {
        arg <- args if arg.contains(StringPool.EQUALS)
        kv = arg.split(StringPool.EQUALS)
      } yield (kv(0), kv(1))
      ).toMap
  }
}

object Sparkle {

  def apply(args: Array[String]): Sparkle = new Sparkle(args)

}
