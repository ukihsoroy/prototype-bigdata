package io.github.ukihsoroy.bigdata.framework.spark

import java.util.ResourceBundle

import io.github.ukihsoroy.bigdata.framework.enums.{EnvType, SparkType}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.StreamingContext

import scala.util.hashing.Hashing.Default

/**
 * description: Sparkle <br>
 * date: 7/20/2020 21:29 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
class Sparkle(args: Array[String]) extends TSparkle {

  /**
   * emr参数初始化
   */
  var emrParams: Map[String, String] = buildEmrParams(args)

  /**
   * env初始化
   */
  var envParams: Map[String, String] = buildEnvParams()

  var conf: SparkConf = _

  var sc: SparkSession = _

  var ssc: StreamingContext = _

  {

    conf = new SparkConf()

    val env = getParameter("env", "dev")
    if (EnvType.DEV.toString.equals(env)) {
      conf.setAppName(getParameter("appName")).setMaster("local[2]")
    } else {
      conf.setAppName(getParameter("appName"))
    }

    val sparkType = getParameter("sparkType", SparkType.SQL.toString)
    if (SparkType.SQL.toString.equals(sparkType)) {

    }

  }

  def buildEnvParams(): Map[String, String] = {
    Map()
  }

  def buildEmrParams(args: Array[String]): Map[String, String] = {
    (
      for {
        arg <- args if arg.contains("=")
        kv = arg.split("=")
      } yield (kv(0), kv(1))
    ).toMap
  }

  def getParameter(key: String, default: String = ""): String = emrParams.getOrElse(key, envParams.getOrElse(key, default))

}

object Sparkle {

  def apply(args: Array[String]): Sparkle = new Sparkle(args)
  
}
