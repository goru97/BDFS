package com.rackspacecloud.detector.ml

import com.rackspacecloud.detector.vo.MetricList
import org.apache.spark.mllib.clustering.KMeansModel

/**
 * Created by gauravbajaj on 4/29/16.
 */
object Detector {
  def detectForModel(throughput:Double, model:KMeansModel, metricList:MetricList) {
    println(metricList.metrics.apply(0).metricName)
  }
}
