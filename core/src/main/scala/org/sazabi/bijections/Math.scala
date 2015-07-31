package org.sazabi.bijections

import com.twitter.bijection._
import java.math.MathContext.UNLIMITED
import org.sazabi.bijections.util.BigDecimalUtil

trait ScalaMathInjections {
  implicit val intToBigIntInjection: Injection[Int, BigInt] =
    Injection.build[Int, BigInt](BigInt(_))(b =>
      Inversion.attemptWhen(b)(_.isValidInt)(_.intValue))

  implicit val bigIntToStringInjection: Injection[BigInt, String] =
    Injection.buildCatchInvert[BigInt, String](_.toString)(BigInt(_))

  implicit val bigIntToByteArrayInjection: Injection[BigInt, Array[Byte]] =
    Injection.buildCatchInvert[BigInt, Array[Byte]](_.toByteArray)(BigInt(_))

  implicit val intToBigDecimalInjection: Injection[Int, BigDecimal] =
    Injection.buildCatchInvert[Int, BigDecimal](BigDecimal(_))(_.toIntExact)

  implicit val longToBigDecimalInjection: Injection[Long, BigDecimal] =
    Injection.buildCatchInvert[Long, BigDecimal](BigDecimal(_))(_.toLongExact)

  implicit val bigDecimalToStringInjection: Injection[BigDecimal, String] =
    Injection.buildCatchInvert[BigDecimal, String](_.toString)(
      BigDecimalUtil.exact(_))

  implicit val bigIntToBigDecimalInjection: Injection[BigInt, BigDecimal] =
    Injection.buildCatchInvert[BigInt, BigDecimal](BigDecimalUtil.exact(_))(bd =>
      new BigInt(bd.bigDecimal.toBigIntegerExact()))
}

object math extends ScalaMathInjections
