package com.rackspacecloud.warzone.services.http

/**
 * Created by gauravbajaj on 5/6/16.
 */

import com.rackspacecloud.warzone.ml.Trainer
import com.rackspacecloud.warzone.utils.Config
import spray.http.StatusCodes
import akka.actor.Actor
import akka.event.Logging

import spray.routing.RequestContext

object WarzoneHTTPService {
  case class Process(from: Long, to: Long)
}

class WarzoneHTTPService(requestContext: RequestContext) extends Actor {

  import WarzoneHTTPService._

  implicit val system = context.system
  val log = Logging(system, getClass)

  def receive = {
    case Process(from,to) =>
      process(from,to)
      context.stop(self)
  }

  def process(from: Long, to: Long) = {
    log.info("Requested model training from : {}, to: {}", from, to)
    if (to < from) {
      requestContext.complete(StatusCodes.BadRequest, "End time must be greater than start time")
    } else if ((to - from) > Config.MAX_TRAIN_TIME) {
      requestContext.complete(StatusCodes.BadRequest, "Training period should not be greater than " +
        Config.MAX_TRAIN_TIME)
    } else if (System.currentTimeMillis - from > Config.MAX_TRAIN_TIME) {
      print(System.currentTimeMillis - from)
      requestContext.complete(StatusCodes.BadRequest, "Start time cannot be older than two days")
    } else {
      val thread = new Thread(new Runnable {
        override def run() {
          Trainer.trainModel(from, to)
        }
      })
      thread.start()
      requestContext.complete(StatusCodes.Accepted, "Training the model for time periods from: " + from + " to: " + to)
    }
    }
}
