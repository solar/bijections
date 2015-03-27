package org.sazabi.bijections.argonaut

import scala.util.{ Failure, Success }

import _root_.argonaut._
import com.twitter.bijection.{ Injection, InversionFailure }
import scalaz.{ Failure => _, Success => _, _ }
import syntax.validation._

trait Injections {
  implicit def JsonToString[A]: Injection[Json, String] =
    Injection.build[Json, String](json => json.nospaces)(str =>
        Parse.parse(str).fold(e => Failure(InversionFailure(e, null)),
          v => Success(v)))

  def CodecJsonToInjection[A](implicit c: CodecJson[A]): Injection[A, Json] =
    Injection.build[A, Json](c.encode(_))(j => j.jdecode.result match {
      case -\/((e, c)) => Failure(InversionFailure(e, null))
      case \/-(a) => Success(a)
    })
}

object injections extends Injections
