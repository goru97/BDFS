package com.rackspacecloud.detector.vo

import spray.json.{JsValue, RootJsonFormat, DefaultJsonProtocol}

/**
 * Created by gauravbajaj on 4/29/16.
 */
case class RawMetric (collectionTime:BigInt, metricName:String, ttlInSeconds:BigInt, metricValue:Double)
case class MetricList(metrics: List[RawMetric])

object MetricJsonProtocol extends DefaultJsonProtocol {
  implicit val metricFormat = jsonFormat4(RawMetric)
  implicit object metricListJsonFormat extends RootJsonFormat[MetricList] {
    def read(value: JsValue) = MetricList(value.convertTo[List[RawMetric]])
    def write(f: MetricList) = ???
  }
}