package io.github.ukihsoroy.bigdata.framework.enums

object ErrorMsg extends Enumeration {
  type ErrorMsgEnum = Value
  val gtMaxCnt: ErrorMsg.Value = Value("count is greater then")
}
