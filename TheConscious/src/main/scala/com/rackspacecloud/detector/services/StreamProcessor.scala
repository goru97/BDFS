package com.rackspacecloud.detector.services

import com.rackspacecloud.detector.io.{CassandraIO, SparkContextProvider}
import com.rackspacecloud.detector.ml.Detector
import com.rackspacecloud.detector.utils.{MetricParser, Config}
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.kafka.KafkaUtils


/**
 * Created by gauravbajaj on 4/27/16.
 */
object StreamProcessor {
  val cassandraIO = new CassandraIO
  val sparkConf = SparkContextProvider.sparkConf
  val ssc = SparkContextProvider.streamingContext

  def main(args: Array[String]) {
    val Array(brokers, topics) = Array(Config.BROKERS, "123")

    val topicsSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topicsSet)

    val thresholdAndModel = cassandraIO.fetchThresholdModel()

    messages.foreachRDD(message => {
      if (message.collect().length > 0) {
        val metriclist = MetricParser.messageToMetrics(message.collect().apply(0)._2)
        Detector.detectForModel(thresholdAndModel._1, thresholdAndModel._2, metriclist)
      }
      else {
        println("No new metrics")
      }
    })

    ssc.start()
    ssc.awaitTermination()

  }
}
