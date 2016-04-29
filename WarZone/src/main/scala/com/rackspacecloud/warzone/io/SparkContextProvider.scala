package com.rackspacecloud.warzone.io

import com.rackspacecloud.warzone.utils.Config
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by gauravbajaj on 3/19/16.
 */

object SparkContextProvider {
  val CassandraSeedHost = "104.130.20.82"
  val SparkCleanerTtl = 3600
  val config = new Config()
  val _conf = new SparkConf().setAppName(config.APP_NAME).setMaster(config.MASTER_URL).set("spark.cassandra.connection.host", CassandraSeedHost).set("spark.cleaner.ttl", SparkCleanerTtl.toString)
  val _sc = new SparkContext(_conf)

  def sparkContext = _sc

  def sparkConf = _conf
}

