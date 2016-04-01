package com.rackspacecloud.warzone.ml

import com.rackspacecloud.warzone.io.SparkIO
import org.apache.spark.mllib.clustering.{KMeansModel, KMeans}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.{Vectors, Vector}

/**
 * Created by gauravbajaj on 3/28/16.
 */
object Trainer {
  def distance (a:Vector, b:Vector) =
    math.sqrt(a.toArray.zip(b.toArray).
      map(p => p._1 - p._2).map(d => d*d).sum)

  def distToCentroid(v: Vector, model: KMeansModel) = {
    val centroid = model.clusterCenters(model.predict(v))
    distance(centroid, v)
  }

  def clusteringScore(data: RDD[Vector], k: Int) = {
    val kmeans = new KMeans()
    kmeans.setK(k)
    val model = kmeans.run(data)
    data.map(d => distToCentroid(d, model)).mean()
  }

  def convertData(twoDArray: Array[Array[Double]]):RDD[Vector] = {
    val data = twoDArray.map{ featuresRow => Vectors.dense(featuresRow)}
    SparkIO.sparkContext.makeRDD(data)
  }

  def getClusteringScore(data:Array[Array[Double]]) = {
    val vectorizedData = convertData(data)
    (5 to 120 by 5).map(k => (k, clusteringScore(vectorizedData, k))) .foreach(println)
  }
}
