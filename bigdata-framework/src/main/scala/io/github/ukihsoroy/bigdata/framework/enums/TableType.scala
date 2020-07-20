package io.github.ukihsoroy.bigdata.framework.enums

object TableType extends Enumeration {
  type TableTypeEnum = Value
  val TABLE: TableType.Value = Value("table")
  val PARQUET: TableType.Value = Value("parquet")
}
