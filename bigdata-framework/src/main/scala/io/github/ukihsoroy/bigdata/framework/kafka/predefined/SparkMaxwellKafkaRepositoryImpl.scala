package io.github.ukihsoroy.bigdata.framework.kafka.predefined

import com.alibaba.fastjson.JSON
import io.github.ukihsoroy.bigdata.component.util.JavaJsonUtil
import io.github.ukihsoroy.bigdata.framework.bean.MaxwellBean
import io.github.ukihsoroy.bigdata.framework.kafka.impl.SparkKafkaRepositoryImpl
import org.apache.spark.streaming.dstream.DStream

abstract class SparkMaxwellKafkaRepositoryImpl extends SparkKafkaRepositoryImpl[MaxwellBean] {

  override protected def transJson2Bean(jsonStream: DStream[String]): DStream[MaxwellBean] = {
    jsonStream.flatMap {
      x =>
        try {
          Some(JSON.parseObject(x, classOf[MaxwellBean]))
        } catch {
          case e: Exception =>
            logger.error(e.getMessage + ", json string:" + x)
            None
        }
    }
  }

  override protected def transBean2Json(beanStream: DStream[MaxwellBean]): DStream[String] = {
    beanStream.map(x => JavaJsonUtil.toJSONString(x))
  }

}
