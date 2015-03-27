package org.sazabi.bijections.jodatime

import scala.util.Success

import com.twitter.bijection._
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.format.DateTimeFormat
import org.joda.time.LocalDate
import org.scalatest._

class StringSpec extends FlatSpec with Matchers with StringInjections {
  implicit val dateTimeFormat = Tags.ForDateTime(
    DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss").withZone(DateTimeZone.UTC))

  implicit val localDateFormat = Tags.ForLocalDate(
    DateTimeFormat.forPattern("yyyy/MM/dd").withZone(DateTimeZone.UTC))

  val dateTime = new DateTime("2013-10-18T12:34Z", DateTimeZone.UTC)
  val localDate = new LocalDate("2013-10-18")

  val dateTimeString = "2013/10/18 12:34:00"
  val localDateString = "2013/10/18"

  val invalidString = "2013-10-08T12:34:00.000Z"

  "Injection[DateTime, String]" should "convert DateTime to String" in {
    Injection[DateTime, String](dateTime) shouldBe dateTimeString
  }

  it should "invert String to DateTime" in {
    Injection.invert[DateTime, String](dateTimeString) shouldBe
      Success(dateTime)

    Injection.invert[DateTime, String](invalidString) shouldBe 'failure
  }

  "Injection[LocalDate, String]" should "convert LocalDate to String" in {
    Injection[LocalDate, String](localDate) shouldBe localDateString
  }

  it should "invert String to LocalDate" in {
    Injection.invert[LocalDate, String](localDateString) shouldBe
      Success(localDate)

    Injection.invert[LocalDate, String](invalidString) shouldBe 'failure
  }
}
