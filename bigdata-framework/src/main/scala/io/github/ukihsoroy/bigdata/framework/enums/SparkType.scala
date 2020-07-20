package io.github.ukihsoroy.bigdata.framework.enums

/**
 * description: SparkType <br>
 * date: 7/20/2020 22:04 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
object SparkType extends Enumeration {
  type SparkTypeEnum = Value
  val STREAM: SparkType.Value = Value("stream")
  val SQL: SparkType.Value = Value("sql")

  def main(args: Array[String]): Unit = {
    println(SQL.toString)
  }
}
