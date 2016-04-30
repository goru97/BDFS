package com.rackspacecloud.detector.services

import scala.collection.immutable.List

/**
 * Created by gauravbajaj on 4/29/16.
 */
object NetworkService {

 /* val network_metric_list = List(
    "servers.ip-172-31-43-14.network.eth0.rx_byte",
    "servers.ip-172-31-43-14.network.eth0.rx_packets",
    "servers.ip-172-31-43-14.network.eth0.rx_errors",
    "servers.ip-172-31-43-14.network.eth0.rx_bit",
    "servers.ip-172-31-43-14.network.eth0.tx_bit",
    "servers.ip-172-31-43-14.netstat.SYN_SENT",
    "servers.ip-172-31-43-14.netstat.SYN_RECV",
    "servers.ip-172-31-43-14.netstat.CLOSE_WAIT",
    "servers.ip-172-31-43-14.netstat.TIME_WAIT",
    "servers.ip-172-31-43-14.netstat.LAST_ACK",
    "servers.ip-172-31-43-14.netstat.CLOSE",
    "servers.ip-172-31-43-14.netstat.ESTABLISHED"
  )*/

  val network_metric_list = List(
    "servers.ip-172-31-43-14.netstat.SYN_SENT",
    "servers.ip-172-31-43-14.netstat.SYN_RECV",
    "servers.ip-172-31-43-14.netstat.LISTEN",
    "servers.ip-172-31-43-14.netstat.CLOSE_WAIT",
    "servers.ip-172-31-43-14.netstat.TIME_WAIT",
    "servers.ip-172-31-43-14.netstat.LAST_ACK",
    "servers.ip-172-31-43-14.netstat.CLOSING",
    "servers.ip-172-31-43-14.netstat.CLOSE",
    "servers.ip-172-31-43-14.netstat.ESTABLISHED",
    "servers.ip-172-31-43-14.netstat.FIN_WAIT1",
    "servers.ip-172-31-43-14.netstat.FIN_WAIT2"
  )
}
