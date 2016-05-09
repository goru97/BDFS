package com.rackspacecloud.detector.ml

import com.rackspacecloud.detector.io.{TwilioIO, BluefloodIO}
import com.rackspacecloud.detector.utils.Config
import com.rackspacecloud.detector.vo.{RawMetric, MetricList}
import com.rackspacecloud.detector.services.NetworkService.network_metric_list
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.{Vector, Vectors}

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

/**
 * Created by gauravbajaj on 4/29/16.
 */

object Detector {
  def detectForModel(threshold:Double, model:KMeansModel, mean:Array[Double], stddev:Array[Double],
                     metricList:MetricList) {
    val received_metrics = metricList.metrics
    val received_metricValues = new ListBuffer[Double]

    val received_metricsMap = new HashMap[String, RawMetric]
    network_metric_list.foreach(metric => {
      received_metrics.foreach(rawMetric => {
        if(rawMetric.metricName.equals(metric)){
          if(received_metricsMap.get(metric).isEmpty){
            received_metricsMap.+=(metric -> rawMetric)
          } else {   //Pick the latest value of the metric in case metrics are more than 1
            if(received_metricsMap.get(metric).get.collectionTime < rawMetric.collectionTime) {
              received_metricsMap.+=(metric -> rawMetric)
            }
          }
        }
      })
    })

    try {
      network_metric_list.foreach(metric => {
        val received_metric = received_metricsMap(metric)
        if (received_metric != null) {
          received_metricValues += received_metricsMap(metric).metricValue
        }
      }) // Add the collected metrics

    }
    catch {
      case e: NoSuchElementException => println("Key Not Found")
    }
    val datum = received_metricValues.toArray
    println("Length of received_metricValues",datum.length)
    if(datum.length.equals(network_metric_list.length)){
     val incoming_data_point = normalize(datum, stddev, mean)
      val distance_incoming_point = distToCentroid(Vectors.dense(incoming_data_point), model)
      println("incoming_data_point", incoming_data_point)
      println("distance_incoming_point", distance_incoming_point)
      println("threshold", threshold)
      if(distance_incoming_point > threshold) {
        BluefloodIO.saveToBlueflood("{\"what\": \"Network Anomaly\", \"when\": "+System.currentTimeMillis()+",\"tags\": \"Anomaly\",\"data\": \"Anomaly\"}")
        if(Config.TWILIO_ENABLED) {
          new TwilioIO().sendSMS("Network Anomaly")
        }
        println("ANOMALY!")
      }
    }
  }

  def distance (a:Vector, b:Vector) =
    math.sqrt(a.toArray.zip(b.toArray).
      map(p => p._1 - p._2).map(d => d*d).sum)

  def distToCentroid(v: Vector, model: KMeansModel) = {
    val centroid = model.clusterCenters(model.predict(v))
    distance(centroid, v)
  }

  def normalize(datum:Array[Double], stdev:Array[Double], mean:Array[Double]): Array[Double] = {
     (datum, mean, stdev).zipped.map( (datum, mean, stdev) => if (stdev <= 0) (datum - mean) else (datum - mean) / stdev)
  }
}
