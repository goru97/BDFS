package com.rackspacecloud.warzone.io

import com.rackspacecloud.warzone.utils.Config
import org.apache.spark.{SparkContext, SparkConf}

/**
 * Created by gauravbajaj on 3/19/16.
 */

object SparkContextProvider {
  val SparkCleanerTtl = 3600
  val _conf = new SparkConf().setAppName(Config.APP_NAME).setMaster(Config.MASTER_URL).set("spark.cassandra.connection.host", Config.CASSANDRA_SEED_URL).set("spark.cleaner.ttl", SparkCleanerTtl.toString)
  val _sc = new SparkContext(_conf)

  def sparkContext = _sc
  def sparkConf = _conf
}

