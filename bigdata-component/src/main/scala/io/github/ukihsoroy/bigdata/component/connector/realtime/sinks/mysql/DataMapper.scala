package io.github.ukihsoroy.bigdata.component.connector.realtime.sinks.mysql

import java.sql.ResultSet

/**
 * description: DataMapper <br>
 * date: 7/29/2020 17:07 <br>
 *
 * @author K.O <br>
 * @version 1.0 <br>
 */
trait DataMapper[T] {

  def map(resultSet: ResultSet): T

}
