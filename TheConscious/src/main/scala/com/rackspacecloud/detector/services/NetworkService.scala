package com.rackspacecloud.detector.services

import scala.collection.immutable.List

/**
 * Created by gauravbajaj on 4/29/16.
 */
class NetworkService {

  val network_metric_list = List(
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
  )
}
