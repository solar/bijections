package org.sazabi.bijections.argonaut

import scala.util.Success

import _root_.argonaut._
import _root_.argonaut.DecodeJson._
import _root_.argonaut.Json._
import _root_.argonaut.JsonIdentity._
import _root_.scalaz.{ Success => SS, Failure => SF, _ }
import com.twitter.bijection._
import com.twitter.bijection.Conversion.asMethod
import org.scalatest._
import syntax.validation._

class InjectionsSpec extends FlatSpec with Matchers with Injections {
  val json = Json("key1" -> jNumberOrNull(20), "key2" -> jString("hoge"))
  val valid = json.nospaces
  val invalid = """{"invalid json"}"""

  case class Hoge(str: String)

  implicit val inj: Injection[Hoge, String] =
    Injection.buildCatchInvert[Hoge, String](_.str)(
      s => if (s == "hoge") Hoge(s) else throw new RuntimeException)

  "Injection[Json, String]" should "convert Json to String" in {
    Injection[Json, String](json) shouldBe valid
  }

  it should "invert valid string to Success(Json)" in {
    Injection.invert[Json, String](valid) shouldBe Success(json)
  }

  it should "invert invalid string to Failure" in {
    Injection.invert[Json, String](invalid) shouldBe 'failure
  }

  "CodecJsonToInjection[A]" should "create Injection[A, Json] from CodecJson[A]" in {
    implicit val cj: CodecJson[Hoge] = CodecJson(v => jString(v.str), { c =>
      c.focus.string match {
        case Some(s) if s == "hoge" => DecodeResult.ok(Hoge(s))
        case _ => DecodeResult.fail("Invalid json for Hoge", c.history)
      }
    })

    val inj: Injection[Hoge, Json] = CodecJsonToInjection[Hoge]

    inj(Hoge("hoge")).string shouldBe Some("hoge")

    inj.invert(jString("hoge")) shouldBe Success(Hoge("hoge"))
    inj.invert(jString("fuga")) shouldBe 'failure
  }
}
