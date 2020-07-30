package io.github.ukihsoroy.bigdata.framework.kafka.offset

import java.util

import io.github.ukihsoroy.bigdata.framework.hbase.THBaseRepository
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Get, Result, Table}
import org.apache.hadoop.hbase.util.Bytes

/**
 * description: FormOffsertHBaseService <br>
 * date: 7/28/2020 21:17 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
object FormOffsetHBaseRepository extends THBaseRepository {

  //hbase table
  private val KAFKA_OFFSET_TABLE = TableName.valueOf("t_kafka_manual_commit_offset")

  //partition
  private val cf = Bytes.toBytes("p")

  /**
   * 通过topic + group获取offset
   * @param rowkey topic + group
   * @return
   */
  def get(rowkey: String): util.NavigableMap[Array[Byte], Array[Byte]] = {
    var r: Result = null
    val (conn, table) = getTable
    var p: util.NavigableMap[Array[Byte], Array[Byte]] = null
    try {
      val r = table.get(new Get(Bytes.toBytes(rowkey)))
      if (null != r) p = r.getFamilyMap(cf)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
    } finally {
      if (conn != null) conn.close()
      if (table != null) table.close()
    }
    p
  }

  private def getTable: (Connection, Table) = {
    val conf = HBaseConfiguration.create()
    val conn = ConnectionFactory.createConnection(conf)
    (conn, conn.getTable(KAFKA_OFFSET_TABLE))
  }

}
