package io.github.ukihsoroy.bigdata.component.util

import scalaj.http.{Http, HttpOptions}

object NetUtil {
  def postRequest(host: String, params: String) = {
    Http(host).postData(params)
      .header("Content-Type", "application/json")
      .header("Charset", "UTF-8")
      .option(HttpOptions.readTimeout(10000)).asString
  }
}
