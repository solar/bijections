package org.sazabi.bijections.jodatime

import java.sql.{ Date, Timestamp }

import com.twitter.bijection._
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.format.{DateTimeFormat => DTFormat}
import org.joda.time.LocalDate
import org.scalatest._

class SqlSpec extends FlatSpec with Matchers with SqlBijections {
  implicit val timeZone = Tags.ForLocalDate(DateTimeZone.UTC)
  val now = 1382054400000L

  val dateTime = new DateTime(now)
  val localDate = new LocalDate(now)

  val timestamp = new Timestamp(now)
  val date = new Date(now)

  "Bijection[DateTime, Timestamp]" should "convert DateTime to Timestamp" in {
    Bijection[DateTime, Timestamp](dateTime) shouldBe timestamp
  }

  it should "invert Timestamp to DateTime" in {
    Bijection.invert[DateTime, Timestamp](timestamp) shouldBe dateTime
  }

  "Bijection[LocalDate, Date]" should "convert LocalDate to Date" in {
    Bijection[LocalDate, Date](localDate) shouldBe date
  }

  it should "invert Date to LocalDate" in {
    Bijection.invert[LocalDate, Date](date) shouldBe localDate
  }
}
