package io.github.ukihsoroy.bigdata.framework.enums

/**
 * description: EnvType <br>
 * date: 7/20/2020 21:57 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
object EnvType extends Enumeration {
  type EnvTypeEnum = Value
  val DEV: EnvType.Value = Value("dev")
  val UAT: EnvType.Value = Value("uat")
  val SIT: EnvType.Value = Value("sit")
  val PROD: EnvType.Value = Value("prod")
}
