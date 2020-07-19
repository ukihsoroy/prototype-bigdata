package io.github.ukihsoroy.bigdata.framework.kafka.predefined

import com.alibaba.fastjson.JSON
import io.github.ukihsoroy.bigdata.component.util.JavaJsonUtil
import io.github.ukihsoroy.bigdata.framework.bean.MaxwellBean
import io.github.ukihsoroy.bigdata.framework.kafka.impl.FlinkTKafkaRepositoryImpl
import org.apache.flink.streaming.api.scala._

abstract class FlinkMaxwellKafkaRepositoryImpl extends FlinkTKafkaRepositoryImpl[MaxwellBean] {

  override protected def transJson2Bean(jsonStream: DataStream[String]): DataStream[MaxwellBean] = {
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

  override protected def transBean2Json(beanStream: DataStream[MaxwellBean]): DataStream[String] = {
    beanStream.map(x => JavaJsonUtil.toJSONString(x))
  }

}
