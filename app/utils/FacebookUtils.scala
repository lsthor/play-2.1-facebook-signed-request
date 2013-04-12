package utils

import play.api._
import play.api.libs.json._

import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

object FacebookUtils {
	def base64UrlDecode(input:String) = {
		new Base64(true).decode(input)
	}

	def parseSignedRequest(input:String, secret:String, maxAge:Option[Int]=None) = {
		val split = input.split("[.]", 2)
		val encodedSig = split(0)
	    val encodedEnvelope = split(1)
	    val envelope = Json.parse(base64UrlDecode(encodedEnvelope))
	    val algorithm = (envelope \ "algorithm").as[String]
	   	val issuedAt = (envelope \ "issued_at").as[Long]

	   	if (!algorithm.equals("HMAC-SHA256")) {
	      throw new Exception("Invalid request. (Unsupported algorithm.)")
	    }

	    if (maxAge.isDefined && issuedAt < System.currentTimeMillis() / 1000 - maxAge.get) {
	      throw new Exception("Invalid request. (Too old.)")
	    }

	   	val key = secret.getBytes()
	    val hmacKey = new SecretKeySpec(key, "HMACSHA256")
	    val mac = Mac.getInstance("HMACSHA256")
	    mac.init(hmacKey)
	    val digest = mac.doFinal(encodedEnvelope.getBytes())

	    if (!java.util.Arrays.equals(base64UrlDecode(encodedSig), digest)) {
	      throw new Exception("Invalid request. (Invalid signature.)")
	    }
	    envelope
	}
}