package org.sazabi.bijections.jodatime

import java.sql.{ Date, Timestamp }

import com.github.nscala_time.time.Imports._
import com.twitter.bijection._
import scalaprops._, Property.forAll
import scalaz._, std.string._, syntax.tag._

object SqlTest extends Scalaprops with SqlBijections {
  implicit val timeZone = Tags.ForLocalDate(DateTimeZone.UTC)

  val bijectionDateTime2Timestamp = {
    val bij = dateTime2Timestamp

    val bijection = forAll { (v: DateTime) =>
      bij(v) == new Timestamp(v.getMillis)
    }(Gens.dateTime).toProperties("o.j.t.DateTime -> j.s.Timestamp")

    val inversion = forAll { (v: Timestamp) =>
      bij.invert(v) == new DateTime(v.getTime)
    }(Gens.sqlTimestamp).toProperties("j.s.Timestamp -> o.j.t.DateTime")

    Properties.fromProps("Bijection[o.j.t.DateTime, j.s.Timestamp]",
      bijection, inversion)
  }

  val bijectionLocalDate2Date = {
    val bij = localDate2Date

    val bijection = forAll { (v: LocalDate) =>
      bij(v) == new Date(v.toDateTimeAtStartOfDay(timeZone.unwrap).getMillis)
    }(Gens.localDate).toProperties("o.j.t.LocalDate -> j.s.Date")

    val inversion = forAll { (v: Date) =>
      bij.invert(v) == new DateTime(v.getTime, timeZone.unwrap).toLocalDate
    }(Gens.sqlDate).toProperties("j.s.Date -> o.j.t.LocalDate")

    Properties.fromProps("Bijection[o.j.t.LocalDate, j.s.Date]",
      bijection, inversion)
  }
}
