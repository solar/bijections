package org.sazabi.bijections.jodatime

import java.sql.{ Date, Timestamp }

import com.github.nscala_time.time.Imports._
import com.twitter.bijection.Bijection
import scalaz._

import org.sazabi.bijections.jodatime.Tags._

trait SqlBijections {
  implicit val dateTime2Timestamp: Bijection[DateTime, Timestamp] =
    Bijection.build[DateTime, Timestamp](dt => new Timestamp(dt.getMillis))(
      ts => new DateTime(ts.getTime))

  implicit def localDate2Date(implicit zone: DateTimeZone @@ ForLocalDate): Bijection[LocalDate, Date] = {
    val z = ForLocalDate.unwrap(zone)

    val apply: LocalDate => Date = ld =>
      new Date(ld.toDateTimeAtStartOfDay(z).getMillis)

    val unapply: Date => LocalDate = d => new DateTime(d.getTime, z).toLocalDate

    Bijection.build(apply)(unapply)
  }
}

object sql extends SqlBijections
