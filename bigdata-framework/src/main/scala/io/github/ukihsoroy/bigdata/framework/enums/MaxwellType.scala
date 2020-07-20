package io.github.ukihsoroy.bigdata.framework.enums

object MaxwellType extends Enumeration {
  type MaxwellTypeEnum = Value
  val INSERT: MaxwellType.Value = Value("insert")
  val UPDATE: MaxwellType.Value = Value("update")
  val DELETE: MaxwellType.Value = Value("delete")
}
