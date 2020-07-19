package io.github.ukihsoroy.bigdata.framework.enums

object TableType extends Enumeration {
  type TableTypeEnum = Value
  val TABLE = Value("table")
  val PARQUET = Value("parquet")
}
