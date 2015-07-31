package org.sazabi.bijections.jodatime

import com.github.nscala_time.time.Imports._
import com.twitter.bijection.Injection
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import scalaz._

import org.sazabi.bijections.jodatime.Tags._

trait StringInjections {
  private[this] val dateTimeFormat = ForDateTime(ISODateTimeFormat.dateTime)
  private[this] val localDateFormat = ForLocalDate(ISODateTimeFormat.date)

  implicit def dateTime2String(implicit fmt: DateTimeFormatter @@ ForDateTime = dateTimeFormat): Injection[DateTime, String] = {
    val f = ForDateTime.unwrap(fmt)
    Injection.buildCatchInvert[DateTime, String](f.print(_))(f.parseDateTime(_))
  }

  implicit def localDate2String(implicit fmt: DateTimeFormatter @@ ForLocalDate = localDateFormat): Injection[LocalDate, String] = {
    val f = ForLocalDate.unwrap(fmt)
    Injection.buildCatchInvert[LocalDate, String](f.print(_))(f.parseLocalDate(_))
  }
}

object string extends StringInjections
