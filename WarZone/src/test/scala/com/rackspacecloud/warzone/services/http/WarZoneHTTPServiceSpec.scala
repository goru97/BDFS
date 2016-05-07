package com.rackspacecloud.warzone.services.http

import com.rackspacecloud.warzone.services.WarzoneService
import com.rackspacecloud.warzone.utils.Config
import org.scalatest.{Matchers, FreeSpec}
import spray.http.StatusCodes._
import spray.testkit.ScalatestRouteTest

/**
 * Created by gauravbajaj on 5/6/16.
 */
class WarZoneHTTPServiceSpec extends FreeSpec with WarzoneService with ScalatestRouteTest with Matchers {
  def actorRefFactory = system

  val to1 = System.currentTimeMillis()
  val from1 = System.currentTimeMillis-86400

  "The Training Service" - {

    "when calling GET api/train/"+from1+"/"+to1 - {
      "should return 'Training the model for time periods from: "+from1+" to: '"+to1 in {
        Get(("/api/train/"+from1+"/"+to1)) ~> sprayApiRoute ~> check {
          status should equal(Accepted)
          entity.toString should include ("Training the model for time periods from: "+from1+" to: "+to1)
        }
      }
    }

    val from2 = System.currentTimeMillis-172900
    val to2 = System.currentTimeMillis()

    "when calling GET api/train/"+from2+"/"+to2 - {
      "should return 'Training period should not be greater than '"+Config.MAX_TRAIN_TIME in {
        Get(("/api/train/"+from2+"/"+to2)) ~> sprayApiRoute ~> check {
          status should equal(BadRequest)
          entity.toString should include ("Training period should not be greater than "+Config.MAX_TRAIN_TIME)
        }
      }
    }
  }
}
