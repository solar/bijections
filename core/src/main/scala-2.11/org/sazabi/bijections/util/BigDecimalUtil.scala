package org.sazabi.bijections.util

import java.math.{ BigDecimal => BigDec, MathContext }

object BigDecimalUtil {
  def decimal(d: Double, mc: MathContext): BigDecimal = BigDecimal.decimal(d, mc)

  def decimal(d: Double): BigDecimal = BigDecimal.decimal(d)

  def exact(repr: BigDec): BigDecimal = BigDecimal.exact(repr)

  def exact(d: Double): BigDecimal = BigDecimal.exact(d)

  def exact(bi: BigInt): BigDecimal = BigDecimal.exact(bi)

  def exact(s: String): BigDecimal = BigDecimal.exact(s)

  def exact(cs: Array[Char]): BigDecimal = BigDecimal.exact(cs)
}
