package org.sazabi.bijections.scalaz

import scala.util.{ Failure, Success }

import com.twitter.bijection.Injection
import _root_.scalaz.\/

object Disjunction {
  def injection[A, B](to: A => B)(from: B => Throwable \/ A): Injection[A, B] =
    Injection.build[A, B](to)(from(_).fold(Failure.apply, Success.apply))
}
