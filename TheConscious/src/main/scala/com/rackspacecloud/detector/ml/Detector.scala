package com.rackspacecloud.detector.ml

import com.rackspacecloud.detector.vo.MetricList
import com.rackspacecloud.detector.services.NetworkService.network_metric_list
import org.apache.spark.mllib.clustering.KMeansModel

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Created by gauravbajaj on 4/29/16.
 */
object Detector {
  def detectForModel(throughput:Double, model:KMeansModel, mean:Array[Double], stddev:Array[Double],
                     metricList:MetricList) {
    val received_metrics = metricList.metrics
    val received_metricValues = new ListBuffer[Double]
    val received_metricsMap = new HashMap[String, BigInt]
    network_metric_list.foreach(metric => {
      received_metrics.foreach(rawMetric => {
        if(rawMetric.metricName.equals(metric)){
          if(received_metricsMap.get(metric).isEmpty){
            received_metricValues+=rawMetric.metricValue
          }
          received_metricsMap.+=(metric -> rawMetric.collectionTime)
        }
      })
    })
    println("Length of received_metricValues", received_metricValues.toArray.length)
    if(received_metricValues.toArray.length.equals(network_metric_list.length)){
      received_metrics.foreach(println)
    }
  }
}
