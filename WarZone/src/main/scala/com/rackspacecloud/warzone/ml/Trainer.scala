package com.rackspacecloud.warzone.ml

import java.io.ObjectOutputStream
import com.rackspacecloud.warzone.io._
import com.rackspacecloud.warzone.services.network.NetworkService
import org.apache.spark.mllib.clustering.{KMeansModel, KMeans}
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.linalg.{Vectors, Vector}
import org.apache.cassandra.io.util.FastByteArrayOutputStream
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
    SparkContextProvider.sparkContext.makeRDD(data) // OR SparkIO.sparkContext.parallelize(data)
  }


  //def normalize(f:Array[Double]) =
  def getClusteringScore(data:RDD[Vector]) = {
    (5 to 120 by 5).par.map(k => (k, clusteringScore(data, k))).minBy(_._2)
    //(5 to 120 by 5).map(k => (k, clusteringScore(vectorizedData, k))).minBy(_._2)
    //(1 to 10 by 1).map(k => (k, clusteringScore(vectorizedData, k))).foreach(println)

  }

  def getThreshold(data:RDD[Vector], model: KMeansModel):Double ={
    val distances = data.map(
      datum => distToCentroid(datum, model)
    )
    distances.top(100).last
  }

  def modelToByteArray(model:KMeansModel): Array[Byte] ={
    val bos = new FastByteArrayOutputStream()
    val oos = new ObjectOutputStream(bos);
    oos.writeObject(model);
    oos.close;
    bos.toByteArray(); //Save model as blob
  }

  def doubleToByteArray(array:Array[Double]): Array[Byte] ={
    array.map(value => value.toByte)
  }

  def trainModel() = {
    val data = new NetworkService().getStitchedNetwork()
    val normalizedData = ZScorer.getNormalizedData(data)
    val vectorizedData = convertData(normalizedData._1)
    val mean = normalizedData._2
    val std = normalizedData._3
    val clusterAndScore = getClusteringScore(vectorizedData)
    val kmeans = new KMeans()
        kmeans.setK(clusterAndScore._1)
    val model = kmeans.run(vectorizedData)
    val threshold = getThreshold(vectorizedData, model)
    val modelArray = modelToByteArray(model)
    new CassandraIO().saveModelandThroughput("123", "NetStat", threshold, modelArray, doubleToByteArray(mean), doubleToByteArray(std))
  }
}
