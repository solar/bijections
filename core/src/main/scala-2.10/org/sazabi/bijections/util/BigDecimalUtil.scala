package org.sazabi.bijections.util

import java.lang.{ Double => JDouble }
import java.math.{ BigDecimal => BigDec, MathContext }

object BigDecimalUtil {
  val defaultMathContext = MathContext.DECIMAL128
  def decimal(d: Double, mc: MathContext): BigDecimal =
    new BigDecimal(new BigDec(JDouble.toString(d), mc), mc)

  def decimal(d: Double): BigDecimal = decimal(d, defaultMathContext)

  def exact(repr: BigDec): BigDecimal = {
    val mc = 
      if (repr.precision <= defaultMathContext.getPrecision) defaultMathContext
      else new MathContext(repr.precision, java.math.RoundingMode.HALF_EVEN)
      new BigDecimal(repr, mc)
  }

  def exact(d: Double): BigDecimal = exact(new BigDec(d))

  def exact(bi: BigInt): BigDecimal = exact(new BigDec(bi.bigInteger))

  def exact(s: String): BigDecimal = exact(new BigDec(s))

  def exact(cs: Array[Char]): BigDecimal = exact(new BigDec(cs))
}
