package io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.mysql

import java.sql.{Connection, DriverManager}

class MysqlSink(host: String, port: String, db: String, user: String, pwd: String) extends Serializable {
  lazy val connection: Connection = {
    val connStr = s"jdbc:mysql://$host:$port/$db"
    val conn = DriverManager.getConnection(connStr, user, pwd)
    sys.addShutdownHook {
      conn.close()
    }
    conn
  }

  /**
   *
   * @param sql sql
   * @param dataMapper mapper
   * @tparam T
   * @return
   */
  def query[T](sql: String)(implicit dataMapper: DataMapper[T]): Option[Seq[T]] = {
    val ps = connection.prepareStatement(sql)
    Class.forName("com.mysql.jdbc.Driver")
    try {
      val resultList = new collection.mutable.ListBuffer[T]
      val resultSet = ps.executeQuery()
      while (resultSet.next()) {
        resultList += dataMapper.map(resultSet)
      }
      Some(resultList)
    }
    catch {
      case e: Exception => e.printStackTrace()
        None
    } finally {
      if (ps != null) {
        ps.close()
      }
    }
  }

}

object MysqlSink {
  def apply(host: String, port: String, db: String, user: String, pwd: String): MysqlSink = new MysqlSink(host, port, db, user, pwd)
}
