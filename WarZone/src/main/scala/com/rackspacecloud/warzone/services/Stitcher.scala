package com.rackspacecloud.warzone.services

import com.rackspacecloud.warzone.services.network.NetworkService
import org.apache.spark.mllib.linalg._

/**
 * Created by gauravbajaj on 3/28/16.
 */
object Stitcher{

  def stitch(arrays:Array[Double]*):Array[Array[Double]] ={
    arrays.transpose.toArray.map( seq => seq.toArray)
  }
}