package com.rackspacecloud.warzone.services.network

import com.rackspacecloud.warzone.io.CassandraFetcher
import com.rackspacecloud.warzone.services.Stitcher
import org.apache.spark.mllib.linalg.Vectors

/**
 * Created by gauravbajaj on 3/28/16.
 */
class NetworkService {

  def getBytesReceived():Array[Double] = {
    val cf = new CassandraFetcher()
    cf.fetchDataForMetric("123.servers.datastax-write.network.bond0.rx_byte")
  }

  def getPacketsReceived():Array[Double] = {
    val cf = new CassandraFetcher()
    cf.fetchDataForMetric("123.servers.datastax-write.network.bond0.rx_packets")
  }

  def getErrorsReceived():Array[Double] = {
    val cf = new CassandraFetcher()
    cf.fetchDataForMetric("123.servers.datastax-write.network.bond0.rx_errors")
  }

  def getStitchedNetwork():Array[Array[Double]] = {
   Stitcher.stitch(getBytesReceived, getPacketsReceived, getErrorsReceived)
  }

}
