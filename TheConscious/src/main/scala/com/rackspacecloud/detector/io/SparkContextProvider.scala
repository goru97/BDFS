package com.rackspacecloud.detector.io

/**
 * Created by gauravbajaj on 4/27/16.
 */

import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.rackspacecloud.detector.utils.Config
import org.apache.spark.{SparkContext, SparkConf}

object SparkContextProvider {
  val SparkCleanerTtl = 3600
  val _conf = new SparkConf().setAppName(Config.APP_NAME).setMaster(Config.MASTER_URL).set("spark.cassandra.connection.host", Config.CASSANDRA_SEED_URL).set("spark.cleaner.ttl", SparkCleanerTtl.toString)
  val _ssc = new StreamingContext(_conf, Seconds(10)) // Check for metrics after every 10 seconds
  def streamingContext = _ssc
  def sparkConf = _conf
}
