package org.sazabi.bijections.jodatime

import java.sql.{ Date, Timestamp }
import scala.collection.JavaConverters._

import scalaprops._
import com.github.nscala_time.time.Imports.{ DateTime, DateTimeZone }

object Gens {
  val DateTimeMax = 4102412399999L

  val dateTime: Gen[DateTime] = for {
    n <- Gen.chooseLong(0L, DateTimeMax)
    d <- Gen.value(new DateTime(n))
  } yield d

  val localDate = dateTime.map(_.toLocalDate)

  val sqlTimestamp: Gen[Timestamp] = for {
    n <- Gen.chooseLong(0L, DateTimeMax)
    ts <- Gen.value(new Timestamp(n))
  } yield ts

  val sqlDate: Gen[Date] = for {
    n <- Gen.chooseLong(0L, DateTimeMax)
    ts <- Gen.value(new Date(n))
  } yield ts

  val zone: Gen[DateTimeZone] = for {
    ids <- Gen.value(DateTimeZone.getAvailableIDs.asScala.toSeq)
    id <- Gen.elements(ids.head, ids.tail: _*)
    zone <- Gen.value(DateTimeZone.forID(id))
  } yield zone
}
