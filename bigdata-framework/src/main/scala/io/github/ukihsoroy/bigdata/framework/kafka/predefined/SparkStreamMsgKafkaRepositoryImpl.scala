package io.github.ukihsoroy.bigdata.framework.kafka.predefined

import com.alibaba.fastjson.JSON
import io.github.ukihsoroy.bigdata.component.util.JavaJsonUtil
import io.github.ukihsoroy.bigdata.framework.bean.StreamMsgBean
import io.github.ukihsoroy.bigdata.framework.kafka.impl.SparkKafkaRepositoryImpl
import org.apache.spark.streaming.dstream.DStream

abstract class SparkStreamMsgKafkaRepositoryImpl extends SparkKafkaRepositoryImpl[StreamMsgBean] {

  override protected def transJson2Bean(jsonStream: DStream[String]): DStream[StreamMsgBean] = {
    jsonStream.flatMap {
      x =>
        try {
          Some(JSON.parseObject(x, classOf[StreamMsgBean]))
        } catch {
          case e: Exception =>
            logger.error(e.getMessage + ", json string:" + x)
            None
        }
    }
  }

  override protected def transBean2Json(beanStream: DStream[StreamMsgBean]): DStream[String] = {
    beanStream.map(x => JavaJsonUtil.toJSONString(x))
  }
}
