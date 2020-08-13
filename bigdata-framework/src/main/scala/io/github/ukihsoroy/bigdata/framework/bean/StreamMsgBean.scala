package io.github.ukihsoroy.bigdata.framework.bean

import com.alibaba.fastjson.{JSON, JSONObject}

class StreamMsgBean extends Serializable {

  /**
    * 业务类型标识
    * ${database}-${table} ，与最终Hive结构表对应
    **/
  var bType: String = _

  /**
    * 数据记录唯一标识
    * 32位uuid，可以为唯一的业务主键，作为kafka消息的key
    **/
  var recordGid: String = _

  /**
    * 数据获取的业务时间
    * 13位时间戳，create_time
    **/
  var gTime: String = _

  /**
    * 数据推送时间
    * 13位时间戳，当前处理时间
    **/
  var uTime: String = _

  /**
    * 业务数据json串
    * 业务数据
    **/
  var data: String = _

  override def toString: String = {
    s"$bType, $recordGid, $gTime, $uTime, $data"
  }

  def toJSONString: String = {
    val json = new JSONObject()
    json.put("bType", bType)
    json.put("recordGid", recordGid)
    json.put("gTime", gTime)
    json.put("uTime", uTime)
    json.put("data", JSON.parseObject(data))
    json.toJSONString
  }

}
