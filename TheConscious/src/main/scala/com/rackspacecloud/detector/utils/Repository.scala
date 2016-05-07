package com.rackspacecloud.detector.utils

/**
 * Created by gauravbajaj on 5/7/16.
 */
import com.google.gson.Gson
import java.io.FileNotFoundException
import java.io.FileReader

import com.rackspacecloud.detector.vo.Administrator

class Repository {


  private var filePath: String = "./src/main/resources/administrators.json"

  def this(filePath: String) {
    this()
    this.filePath = filePath
  }

  def getAdministrators(): Array[Administrator] = {
    try {
      new Gson().fromJson(new FileReader(filePath), classOf[Array[Administrator]])
    } catch {
      case e: FileNotFoundException => {
        e.printStackTrace()
        Array.ofDim[Administrator](0)
      }
    }
  }
}
