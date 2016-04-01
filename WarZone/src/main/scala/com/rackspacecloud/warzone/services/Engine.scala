package com.rackspacecloud.warzone.services

import com.rackspacecloud.warzone.io.SparkIO
import com.rackspacecloud.warzone.ml.Trainer
import com.rackspacecloud.warzone.services.network.NetworkService
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.clustering._
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD

/**
 * Created by gauravbajaj on 3/18/16.
 */


class Engine1 {
 /* val sc = SparkIO.sparkContext
  val rawData = sc.textFile("/Users/gauravbajaj/Downloads/kddcup.data")

  val labelsAndData = rawData.map { line =>
    val buffer = line.split(',').toBuffer
    buffer.remove(1,3)
    val label = buffer.remove(buffer.length-1)
    val vector = Vectors.dense(buffer.map(_.toDouble).toArray)
    (label, vector)
  }

  val _data = labelsAndData.values.cache()

  def data = _data*/
  //val kmeans = new KMeans()
  //val model = kmeans.run(data)

}

object Engine {
/* val engine = new Engine1()

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
 }*/

  def main(args: Array[String]): Unit = {

    /*val data = engine.data
    (5 to 40 by 5).map(k => (k, clusteringScore(data, k))) .foreach(println)*/
     /* new NetworkService().getStitchedNetwork().foreach(y => {
        y.foreach(x => print(x + "\t"))
        println()
      })*/

    Trainer.getClusteringScore(new NetworkService().getStitchedNetwork())
  }
}