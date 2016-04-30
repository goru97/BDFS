package com.rackspacecloud.detector.utils

/**
 * Created by gauravbajaj on 4/29/16.
 */

import com.rackspacecloud.detector.vo.MetricList
import spray.json._
import com.rackspacecloud.detector.vo.MetricJsonProtocol._

object MetricParser {
  def messageToMetrics(message:String):MetricList ={
    val metricsList = message.asJson.convertTo[MetricList]
    metricsList
  }
}
