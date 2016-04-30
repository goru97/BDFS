package com.rackspacecloud.detector.io

import java.io.ObjectInputStream
import java.nio.ByteBuffer

import com.datastax.spark.connector._
import com.datastax.spark.connector.rdd.CassandraRDD
import org.apache.cassandra.io.util.FastByteArrayInputStream
import org.apache.spark.mllib.clustering.KMeansModel
import com.datastax.spark.connector.streaming._

/**
 * Created by gauravbajaj on 4/29/16.
 */
class CassandraIO {

  val sc = SparkContextProvider.streamingContext

  def readRows (keySpace: String, columnFamily: String, tenantId: String): CassandraRDD[CassandraRow] ={
    sc.cassandraTable(keySpace, columnFamily).select("model", "modeltype", "threshold", "mean", "stddev").where("tenantid = ?",tenantId)
  }
  def fetchThresholdModel(): (Double,KMeansModel, Array[Double], Array[Double]) = {
    val rdd = readRows("DATA", "models", "123")
    val row = rdd.collect().apply(0)
    val model = row.getBytes("model")
    val meanBytes = row.getBytes("mean")
    val stdBytes = row.getBytes("stddev")
    val threshold = row.getDouble("threshold")
    val modelArray = model.array()
    val bis = new FastByteArrayInputStream(modelArray)
    val ois = new ObjectInputStream(bis)
    (threshold, ois.readObject().asInstanceOf[KMeansModel], toDoubleArray(meanBytes), toDoubleArray(stdBytes))
  }

  def toDoubleArray(buffer:ByteBuffer): Array[Double] ={
  buffer.array.map(value => value.toDouble)
  }

}
