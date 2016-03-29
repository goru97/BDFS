package com.rackspacecloud.warzone.services

import com.rackspacecloud.warzone.services.network.NetworkService
import org.apache.spark.mllib.linalg._

/**
 * Created by gauravbajaj on 3/28/16.
 */
object Stitcher{
  val network_service = new NetworkService()
}
class Stitcher {
  val bytes_received = Stitcher.network_service.getBytesReceived()
  val errors_received = Stitcher.network_service.getErrorsReceived()

  def printLn = {
   stitch(bytes_received,errors_received).foreach(y => {
     y.foreach(x => print(x + "\t"))
     println()
   })
  }
  def stitch(arrays:Array[Double]*):Array[Array[Double]] ={
   arrays.transpose.toArray.map( seq => seq.toArray)
  }

}
