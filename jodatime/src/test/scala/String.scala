package org.sazabi.bijections.jodatime

import scala.util.Success

import com.github.nscala_time.time.Imports._
import com.twitter.bijection.Injection
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import scalaprops._, Property.forAll
import scalaz._, std.string._, syntax.tag._

object StringTest extends Scalaprops with StringInjections {
  implicit val dateTimeFormat = Tags.ForDateTime(
    DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss.SSS").withZone(DateTimeZone.UTC))

  implicit val localDateFormat = Tags.ForLocalDate(
    DateTimeFormat.forPattern("yyyy/MM/dd").withZone(DateTimeZone.UTC))

  val injectionDateTime2String = {
    val inj = dateTime2String

    val injection = forAll { (v: DateTime) =>
      inj(v) == dateTimeFormat.unwrap.print(v)
    }(Gens.dateTime).toProperties("o.j.t.DateTime -> String")

    val inversion = forAll { (v: DateTime) =>
      val str = dateTimeFormat.unwrap.print(v)
      inj.invert(str).filter(_.isEqual(v)).toOption.isDefined
    }(Gens.dateTime).toProperties("o.j.t.DateTime -> String -> o.j.t.DateTime")

    val failedInversion = forAll { (v: String) =>
      inj.invert(v).toOption.isEmpty
    }(Gen.alphaString).toProperties("fail: random String -> o.j.t.DateTime")

    Properties.fromProps("Injection[o.j.t.DateTime, String]",
      injection, inversion, failedInversion)
  }

  val injectionLocalDate2String = {
    val inj = localDate2String

    val injection = forAll { (v: LocalDate) =>
      inj(v) == localDateFormat.unwrap.print(v)
    }(Gens.localDate).toProperties("o.j.t.LocalDate -> String")

    val inversion = forAll { (v: LocalDate) =>
      val str = localDateFormat.unwrap.print(v)
      inj.invert(str).filter(_ == v).toOption.isDefined
    }(Gens.localDate).toProperties("o.j.t.LocalDate -> String -> o.j.t.LocalDate")

    val failedInversion = forAll { (v: String) =>
      inj.invert(v).toOption.isEmpty
    }(Gen.alphaString).toProperties("fail: random String -> o.j.t.LocalDate")

    Properties.fromProps("Injection[o.j.t.LocalDate, String]",
      injection, inversion, failedInversion)
  }
}
