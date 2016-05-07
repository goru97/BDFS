package com.rackspacecloud.detector.io

import java.util._

import com.rackspacecloud.detector.utils.{Config, Repository}
import com.twilio.sdk.{TwilioRestException, TwilioRestClient}
import org.apache.http.NameValuePair
import org.apache.http.message.BasicNameValuePair

/**
 * Created by gauravbajaj on 5/7/16.
 */

object TwilioIO {
  val accountSid = Config.TWILIO_SID
  val authToken = Config.TWILIO_TOKEN
  val phoneNumber = Config.TWILIO_PHONE
  val client = new TwilioRestClient(accountSid, authToken)
  def getClient = client
  def getPhoneNumber = phoneNumber
}
class TwilioIO {

  def sendSMS(error:String) {

    val message = String.format("It appears the server is having Exception: %s " + "Go to: http://104.130.20.82:8080/BurglaryDetection for more details.", error)
    val administrators = new Repository().getAdministrators
    for (administrator <- administrators) {
      sendMessage(administrator.getPhoneNumber, message)
    }

  }

  def sendMessage(to: String, message: String) {
    val params = getParams(to, message)
    try {
      TwilioIO.getClient.getAccount.getMessageFactory.create(params)
    } catch {
      case exception: TwilioRestException => exception.printStackTrace()
    }
  }

  private def getParams(to: String, message: String): List[NameValuePair] = {
    val params = new ArrayList[NameValuePair]()
    params.add(new BasicNameValuePair("Body", message))
    params.add(new BasicNameValuePair("To", to))
    params.add(new BasicNameValuePair("From", TwilioIO.getPhoneNumber))
    params
  }
}
