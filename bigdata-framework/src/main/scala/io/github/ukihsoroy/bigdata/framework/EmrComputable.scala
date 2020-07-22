package io.github.ukihsoroy.bigdata.framework

import java.util.Properties

import jodd.util.StringPool

/**
 * description: EmrComputable <br>
 * date: 7/20/2020 21:25 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
trait EmrComputable {

  /**
   * emr参数初始化
   */
  protected var emrParams: Map[String, String] = _

  /**
   * env初始化
   */
  protected var envParams: Properties = _

  protected [spark] def buildEnvParams(): Properties = {
    new Properties()
  }

  /**
   * 构建Emr参数
   * @param args
   * @return
   */
  protected [spark] def buildEmrParams(args: Array[String]): Map[String, String] = {
    (
      for {
        arg <- args if arg.contains(StringPool.EQUALS)
        kv = arg.split(StringPool.EQUALS)
      } yield (kv(0), kv(1))
      ).toMap
  }

  /**
   * 获取参数值
   * @param key
   * @param default
   * @return
   */
  def getParameter(key: String, default: String = ""): String = emrParams.getOrElse(key, envParams.getProperty(key, default))

}
