package org.sazabi.bijections.jodatime

import java.sql.{ Date, Timestamp }

import com.twitter.bijection.Bijection
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import scalaz._

trait SqlBijections {
  implicit val dateTime2Timestamp: Bijection[DateTime, Timestamp] =
    Bijection.build[DateTime, Timestamp](dt => new Timestamp(dt.getMillis))(
      ts => new DateTime(ts.getTime))

  implicit def localDate2Date(implicit zone: DateTimeZone @@ Tags.ForLocalDate): Bijection[LocalDate, Date] = {
    val z = Tags.ForLocalDate.unwrap(zone)

    Bijection.build[LocalDate, Date](
      ld => new Date(ld.toDateTimeAtStartOfDay(z).getMillis))(
        d => new DateTime(d.getTime, z).toLocalDate)
  }
}

object sql extends SqlBijections
