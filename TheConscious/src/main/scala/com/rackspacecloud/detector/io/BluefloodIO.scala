package com.rackspacecloud.detector.io

import java.net.URL

import com.rackspacecloud.detector.utils.Config
import org.apache.http.client.methods.HttpPost
import org.apache.http.conn.ClientConnectionManager
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.{DefaultHttpClient, AbstractHttpClient}
import org.apache.http.impl.conn.PoolingClientConnectionManager
import org.apache.http.params.HttpConnectionParams

/**
 * Created by gauravbajaj on 5/1/16.
 */
object BluefloodIO {

  val maxConnectionsPerRoute = 10
  val maxTotalConnections = 10


  def saveToBlueflood(payload:String): Unit ={

    val connManager: ClientConnectionManager = {
      val cm = new PoolingClientConnectionManager()
      cm.setDefaultMaxPerRoute(maxConnectionsPerRoute)
      cm.setMaxTotal(maxTotalConnections)
      cm
    }

    val httpClient: AbstractHttpClient = {
      val client = new DefaultHttpClient(connManager)
      val httpParams = client.getParams
      HttpConnectionParams.setConnectionTimeout(httpParams, 6000)
      HttpConnectionParams.setSoTimeout(httpParams, 6000)
      client
    }

    val req = new HttpPost
    val url = new URL(Config.BLUEFLOOD_URL+"/v2.0/123/events")
    req.setURI(url.toURI)
    val headers: List[(String, String)] = List(("Content-Type", "application/json"))
    headers.foreach { tup: (String, String) =>
      req.addHeader(tup._1, tup._2)
    }
    val body: String = payload
    //oops, sending a request body with a GET request doesn't make sense
    req.setEntity(new StringEntity(body))
    print (body)
    val resp = httpClient.execute(req)
    print(resp.getStatusLine)
  }

}
