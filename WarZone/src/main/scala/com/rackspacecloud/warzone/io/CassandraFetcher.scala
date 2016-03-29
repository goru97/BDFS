package com.rackspacecloud.warzone.io

import java.io.IOException
import java.nio.ByteBuffer

import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.rdd.CassandraRDD
import com.google.protobuf.CodedInputStream
/**
 * Created by gauravbajaj on 3/21/16.
 */

class CassandraFetcher {
  val conf = SparkIO.sparkConf
  val sc = SparkIO.sparkContext
  CassandraConnector(conf).withSessionDo { session =>
    session.execute("CREATE KEYSPACE IF NOT EXISTS DATA WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 1 }")
  }


  def fetchDataForMetric(metric_name: String):Array[Double] ={
    val rdd = readRows("DATA", "metrics_full", metric_name, System.currentTimeMillis() - 172800000, System.currentTimeMillis())
    val metric_values = rdd.collect().map(row => {
    val bb = row.getBytes("value")
    deSerialize(bb)})
    metric_values.map(_.doubleValue())
  }
  def readRows (keySpace: String, columnFamily: String, key: String, timeFrom: Long, timeTo: Long): CassandraRDD[CassandraRow] ={
    sc.cassandraTable(keySpace, columnFamily).select("value").where("key = ? and column1 >= ? and column1 <= ?",key, timeFrom, timeTo)
  }

  def deSerialize (bufferValue: ByteBuffer): Number ={
    val in = CodedInputStream.newInstance(bufferValue.array);

    val version = in.readRawByte() // To get the version
    if (version != 0 ) {
      throw new IOException("Unexpected serialization version")
    }

    val metricValueType = in.readRawByte()
    val value = metricValueType match {
      case 105 => in.readRawVarint32
      case 108 => in.readRawVarint64
      case 110 => in.readDouble
    }
    return value
  }
}