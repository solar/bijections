package org.sazabi.bijections.argonaut

import scala.util.Success

import _root_.argonaut._
import _root_.argonaut.DecodeJson._
import _root_.argonaut.Json._
import _root_.argonaut.JsonIdentity._
import _root_.scalaz.{ Success => SS, Failure => SF, _ }
import com.twitter.bijection._
import scalaprops._, Property.forAll
import syntax.validation._
import std.string._

object InjectionsTest extends Scalaprops with Injections {
  val json = Json("key1" -> jNumberOrNull(20), "key2" -> jString("hoge"))
  val valid = json.nospaces
  val invalid = """{"invalid json"}"""

  val genJNull: Gen[Json] = Gen.value(jNull)

  val genJString: Gen[Json] = Gen.asciiString.map(jString)

  val genJNumber: Gen[Json] = Gen.oneOf(
    Gen[Double].map(jNumberOrString),
    Gen[Long].map(jNumber))

  implicit val genJson: Gen[Json] = Gen.frequency(
    5 -> genJNull,
    30 -> genJNumber,
    30 -> genJString
  )

  private[this] case class Hoge(str: String)

  private[this] object Hoge {
    implicit val codecJson = CodecJson.derive[Hoge]
  }

  val json2String = {
    val inj = JsonToString

    val nospaces = forAll { (v: Json) =>
      inj(v) == v.nospaces
    }.toProperties("Json -> String")

    val invertible = forAll { (v: Json) =>
      inj.invert(v.nospaces) == Success(v)
    }.toProperties("String -> Json")

    val random = forAll { (v: String) =>
      inj.invert(v).toOption == Parse.parse(v).toOption
    }(Gen.asciiString).toProperties("Random string -> Json")

    Properties.fromProps("Injection[Json, String]",
      nospaces, invertible, random)
  }

  val codecJson2Injection = {
    val inj = CodecJsonToInjection[Hoge]

    val encode = forAll { (v: String) =>
      true
    }(Gen.asciiString).toProperties("encode")

    Properties.fromProps("CodecJsonToInjection[A]",
      encode)
  }
}
