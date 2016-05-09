package com.rackspacecloud.warzone.services.network

import com.rackspacecloud.warzone.io.CassandraIO
import scala.collection.immutable.List
import scala.collection.mutable.ListBuffer

/**
 * Created by gauravbajaj on 3/28/16.
 */
class NetworkService {
 /* val network_metric_list = List(
    "123.servers.ip-172-31-43-14.network.eth0.rx_byte",
    "123.servers.ip-172-31-43-14.network.eth0.rx_packets",
    "123.servers.ip-172-31-43-14.network.eth0.rx_errors",
    "123.servers.ip-172-31-43-14.network.eth0.rx_bit",
    "123.servers.ip-172-31-43-14.network.eth0.tx_bit",
    "123.servers.ip-172-31-43-14.netstat.SYN_SENT",
    "123.servers.ip-172-31-43-14.netstat.SYN_RECV",
    "123.servers.ip-172-31-43-14.netstat.CLOSE_WAIT",
    "123.servers.ip-172-31-43-14.netstat.TIME_WAIT",
    "123.servers.ip-172-31-43-14.netstat.LAST_ACK",
    "123.servers.ip-172-31-43-14.netstat.CLOSE",
    "123.servers.ip-172-31-43-14.netstat.ESTABLISHED"
  )*/

  val network_metric_list = List(
    "123.servers.ip-172-31-43-14.netstat.SYN_SENT",
    "123.servers.ip-172-31-43-14.netstat.SYN_RECV",
    "123.servers.ip-172-31-43-14.netstat.LISTEN",
    "123.servers.ip-172-31-43-14.netstat.CLOSE_WAIT",
    "123.servers.ip-172-31-43-14.netstat.TIME_WAIT",
    "123.servers.ip-172-31-43-14.netstat.LAST_ACK",
    "123.servers.ip-172-31-43-14.netstat.CLOSING",
    "123.servers.ip-172-31-43-14.netstat.CLOSE",
    "123.servers.ip-172-31-43-14.netstat.ESTABLISHED",
    "123.servers.ip-172-31-43-14.netstat.FIN_WAIT1",
    "123.servers.ip-172-31-43-14.netstat.FIN_WAIT2"
  )

  def minMax(a: Array[Int]) : (Int, Int) = {
    if (a.isEmpty) throw new java.lang.UnsupportedOperationException("array is empty")
    a.foldLeft((a(0), a(0)))
    { case ((min, max), e) => (math.min(min, e), math.max(max, e))}
  }

  def getStitchedNetwork(from:Long, to:Long):Array[Array[Double]] = {
    val cf = new CassandraIO()
    val network_metrics = ListBuffer[Array[Double]]()
    network_metric_list.foreach(metric => network_metrics+=cf.fetchDataForMetric(metric, from, to))
    var metrics_length_list = ListBuffer[Int]()
    network_metrics.foreach(row => {
      metrics_length_list+=row.length
    })
    val metrics_length_array = metrics_length_list.toArray
    val min_length = minMax(metrics_length_array)._1
    val metrics_result_list = ListBuffer[Array[Double]]()
    network_metrics.foreach(row => {
      metrics_result_list+=row.slice(0,min_length)
    })
    metrics_result_list.toArray.transpose
  }

}
