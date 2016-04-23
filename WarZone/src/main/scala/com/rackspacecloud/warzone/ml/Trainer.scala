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
    val data = twoDArray.map(Vectors.dense(_))
    SparkIO.sparkContext.makeRDD(data) // OR SparkIO.sparkContext.parallelize(data)
  }


  //def normalize(f:Array[Double]) =
  def getClusteringScore(data:Array[Array[Double]]) = {

    val normalizedData = ZScorer.getNormalizedData(data)
    val vectorizedData = convertData(normalizedData)
    //(5 to 120 by 5).par.map(k => (k, clusteringScore(vectorizedData, k))).foreach(println)
    (1 to 10 by 1).map(k => (k, clusteringScore(vectorizedData, k))).foreach(println)

  }
}
