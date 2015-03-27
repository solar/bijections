package org.sazabi.bijections.jodatime

import scalaz.Tag

object Tags {
  sealed trait ForDateTime

  val ForDateTime = Tag.of[ForDateTime]

  sealed trait ForLocalDateTime

  val ForLocalDateTime = Tag.of[ForLocalDateTime]

  sealed trait ForLocalDate

  val ForLocalDate = Tag.of[ForLocalDate]
}
