package org.sazabi.bijections.jodatime

import com.twitter.bijection.Injection
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.LocalDate
import scalaz._, syntax.tag._

trait StringInjections {
  private[this] val dateTimeFormat = Tags.ForDateTime(ISODateTimeFormat.dateTime)
  private[this] val localDateFormat = Tags.ForLocalDate(ISODateTimeFormat.date)

  implicit def dateTime2String(implicit fmt: DateTimeFormatter @@ Tags.ForDateTime = dateTimeFormat): Injection[DateTime, String] = {
    val f = Tags.ForDateTime.unwrap(fmt)
    Injection.buildCatchInvert[DateTime, String](f.print(_))(f.parseDateTime(_))
  }

  implicit def localDate2String(implicit fmt: DateTimeFormatter @@ Tags.ForLocalDate = localDateFormat): Injection[LocalDate, String] = {
    val f = Tags.ForLocalDate.unwrap(fmt)
    Injection.buildCatchInvert[LocalDate, String](f.print(_))(f.parseLocalDate(_))
  }
}

object string extends StringInjections
