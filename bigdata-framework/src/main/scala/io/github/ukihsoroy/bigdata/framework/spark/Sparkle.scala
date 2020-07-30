package io.github.ukihsoroy.bigdata.framework.spark

import java.util.Properties

import io.github.ukihsoroy.bigdata.framework.EmrComputable
import io.github.ukihsoroy.bigdata.framework.enums.{EnvType, SparkType}
import jodd.util.StringPool
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

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

    envParams = buildEnvParams()

    conf = new SparkConf()

    val env = getParameter("env", "dev")
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

  protected def buildEnvParams(): Map[String, String] = {
    null
  }

  /**
   * 构建Emr参数
   * @param args
   * @return
   */
  protected def buildEmrParams(args: Array[String]): Map[String, String] = {
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
