package com.rackspacecloud.warzone.ml

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD

/**
 * Created by gauravbajaj on 4/11/16.
 */
object ZScorer {

  def getSTDevMean(dataArray:Array[Array[Double]]): (Array[Double], Array[Double]) = {
    val numCols = dataArray.apply(0).length
    val n = dataArray.length
    val sums = dataArray.reduce((a,b) => a.zip(b).map(t => t._1 + t._2))
    val sumSquares = dataArray.fold(new Array[Double](numCols))( (a,b) => a.zip(b).map(t => t._1 + t._2 * t._2) )
    val stdev = sumSquares.zip(sums).map { case(sumSq,sum) => math.sqrt(n*sumSq - sum*sum)/n }
    val mean = sums.map(_ / n)
    (stdev, mean)
  }

  def getNormalizedData(dataArray:Array[Array[Double]]):(Array[Array[Double]], Array[Double], Array[Double]) = {
    val t = getSTDevMean(dataArray)
    val mean = t._1
    val stdev = t._2

    def normalize(value: Array[Double]):Array[Double] = {
      (value, mean, stdev).zipped.map( (value, mean, stdev) => if (stdev <= 0) (value - mean) else (value - mean) / stdev)
    }
    (dataArray.map(normalize(_)), mean, stdev)
  }
}
