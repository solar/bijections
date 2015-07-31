package org.sazabi.bijections

import scala.util.Success

import com.twitter.bijection._
import scalaprops._, Property.forAll
import scalaz._, std.anyVal._, std.string._
import org.sazabi.bijections.util.BigDecimalUtil

object MathTest extends Scalaprops with ScalaMathInjections {
  val Unlimited = java.math.MathContext.UNLIMITED

  val bigIntInjections = {
    val int2BigInt = {
      implicit val inj = implicitly[Injection[Int, BigInt]]

      val fromInt = forAll { (i: Int) =>
        inj(i) == BigInt(i)
      }

      val fromSmallBigInt = forAll { (n: Int) =>
        inj.invert(BigInt(n)) == Success(n)
      }

      val fromBigInt = forAll { (n: BigInt) =>
        val result = inj.invert(n)
        if (n.isValidInt) result == Success(n.intValue)
        else result.isFailure
      }

      Properties.properties("Injection[Int, BigInt]")(
        ("Int -> BigInt", fromInt),
        ("small BigInt -> Int", fromSmallBigInt),
        ("BigInt -> Int", fromBigInt)
      )
    }

    val bigInt2String = {
      implicit val inj = implicitly[Injection[BigInt, String]]

      val fromBigInt = forAll { (bi: BigInt) =>
        inj(bi) == bi.toString
      }.toProperties("BigInt -> String")

      val fromString = forAll { (bi: BigInt) =>
        inj.invert(bi.toString) == Success(bi)
      }.toProperties("String -> BigInt")

      val fromNumericString = forAll { (s: String) =>
        val result = inj.invert(s)
        if (s.nonEmpty) result.isSuccess
        else result.isFailure
      }(Gen.numString).toProperties("Numeric String -> BigInt",
        Endo[Param](_.copy(maxSize = 30)))

      val fail = forAll { (s: String) =>
        inj.invert(s).isFailure
      }(Gen.alphaString).toProperties("Fail on invalid string")

      Properties.fromProps("Injection[BigInt, String]",
        fromBigInt, fromString, fromNumericString, fail)
    }

    val bigInt2ByteArray = {
      implicit val inj = implicitly[Injection[BigInt, Array[Byte]]]

      val fromBigInt = forAll { (bi: BigInt) =>
        inj(bi).toSeq == bi.toByteArray.toSeq
      }.toProperties("BigInt -> Array[Byte]")

      val fromByteArray = forAll { (bi: BigInt) =>
        inj.invert(bi.toByteArray) == Success(bi)
      }.toProperties("Array[Byte] -> BigInt")

      val fromRandomByteArray = forAll { (arr: Array[Byte]) =>
        inj.invert(arr).isSuccess
      }(Gen.listOf1(Gen[Byte]).map(_.toList.toArray))
        .toProperties("test", Endo[Param](_.copy(minSize = 1)))

      Properties.fromProps("Injection[BigInt, Array[Byte]]",
        fromBigInt, fromByteArray, fromRandomByteArray)
    }

    Properties.list(
      int2BigInt,
      bigInt2String,
      bigInt2ByteArray
    )
  }

  val bigDecimalInjections = {
    val int2BigDecimal = {
      implicit val inj = implicitly[Injection[Int, BigDecimal]]

      val fromInt = forAll { (n: Int) =>
        inj(n) == BigDecimal(n)
      }.toProperties("Int -> BigDecimal")

      val fromIntDec = forAll { (n: Int) =>
        inj.invert(BigDecimal(n)) == Success(n)
      }.toProperties("Int -> BigDecimal -> Int")

      val fromDec = forAll { (n: BigDecimal) =>
        val result = inj.invert(n)
        if (n.isValidInt) result.isSuccess
        else result.isFailure
      }.toProperties("BigDecimal -> Int")

      Properties.fromProps("Injection[Int, BigDecimal]",
        fromInt, fromIntDec, fromDec)
    }

    val long2BigDecimal = {
      implicit val inj = implicitly[Injection[Long, BigDecimal]]

      val fromLong = forAll { (n: Long) =>
        inj(n) == BigDecimal(n)
      }.toProperties("Long -> BigDecimal")

      val fromLongDec = forAll { (n: Long) =>
        inj.invert(BigDecimal(n)) == Success(n)
      }.toProperties("Long -> BigDecimal -> Long")

      val fromDec = forAll { (bd: BigDecimal) =>
        val result = inj.invert(bd)
        if (bd.isValidLong) result.isSuccess
        else result.isFailure
      }.toProperties("BigDecimal -> Long")

      Properties.fromProps("Injection[Long, BigDecimal]",
        fromLong, fromLongDec, fromDec)
    }

    val bigDecimal2String = {
      implicit val inj = implicitly[Injection[BigDecimal, String]]

      val fromDec = forAll { (bd: BigDecimal) =>
        inj(bd) == bd.toString
      }.toProperties("BigDecimal -> String")

      val fromDecString = forAll { (bd: BigDecimal) =>
        inj.invert(bd.toString) == Success(bd)
      }.toProperties("BigDecimal -> String -> BigDecimal")

      val fromString = forAll { (s: String) =>
        val result = inj.invert(s)
        if (s.nonEmpty) result.isSuccess
        else result.isFailure
      }(Gen.numString).toProperties("Numeric String -> BigDecimal")

      Properties.fromProps("Injection[BigDecimal, String]",
        fromDec, fromDecString, fromString)
    }

    val bigInt2BigDecimal = {
      implicit val inj = implicitly[Injection[BigInt, BigDecimal]]

      val fromBi = forAll { (bi: BigInt) =>
        inj(bi) == BigDecimalUtil.exact(bi)
      }.toProperties("BigInt -> BigDecimal")

      val fromBiDec = forAll { (bi: BigInt) =>
        inj.invert(BigDecimalUtil.exact(bi)) == Success(bi)
      }.toProperties("BigInt -> BigDecimal -> BigInt")

      Properties.fromProps("Injection[BigInt, BigDecimal]",
        fromBi, fromBiDec)
    }

    Properties.list(
      int2BigDecimal,
      long2BigDecimal,
      bigDecimal2String,
      bigInt2BigDecimal
    )
  }
}
