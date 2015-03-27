package org.sazabi.bijections

import scala.util.Success

import com.twitter.bijection._
import org.scalatest._

class MathSpec extends FunSpec with Matchers with ScalaMathInjections {
  describe("BigInt") {
    val ib = BigInt(Int.MaxValue)
    val iba = Array[Byte](127, -1, -1, -1)
    val lb = BigInt(Long.MaxValue)
    val ob = BigInt(Long.MaxValue) * 2

    describe("Injection[Int, BigInt]") {
      it("should convert Int to BigInt") {
        Injection[Int, BigInt](Int.MaxValue) shouldBe ib
      }

      it("should invert BigInt to Int") {
        Injection.invert[Int, BigInt](ib) shouldBe Success(Int.MaxValue)
        Injection.invert[Int, BigInt](lb) shouldBe 'failure
      }
    }

    describe("Injection[BigInt, String]") {
      it("should convert BigInt to String") {
        Injection[BigInt, String](ib) shouldBe Int.MaxValue.toString
      }
    }

    describe("Injection[BigInt, Array[Byte]]") {
      it("should convert BigInt to Array[Byte]") {
        Injection[BigInt, Array[Byte]](ib) shouldBe iba
      }

      it("should invert Array[Byte] to BigInt") {
        Injection.invert[BigInt, Array[Byte]](iba) shouldBe Success(ib)
        Injection.invert[BigInt, Array[Byte]](Array[Byte]()) shouldBe 'failure
      }
    }
  }

  describe("BigDecimal") {
    val ib = BigDecimal(Int.MaxValue)
    val lb = BigDecimal(Long.MaxValue)
    val ob = BigDecimal(Long.MaxValue) * 2

    val db = BigDecimal(Double.MaxValue)

    describe("Injection[Int, BigDecimal]") {
      it("should convert Int to BigDecimal") {
        Injection[Int, BigDecimal](Int.MaxValue) shouldBe ib
      }

      it("should invert BigDecimal to Int") {
        Injection.invert[Int, BigDecimal](ib) shouldBe Success(Int.MaxValue)
        Injection.invert[Int, BigDecimal](lb) shouldBe 'failure
      }
    }

    describe("Injection[Long, BigDecimal]") {
      it("should convert Long to BigDecimal") {
        Injection[Long, BigDecimal](Long.MaxValue) shouldBe lb
      }

      it("should invert BigDecimal to Long") {
        Injection.invert[Long, BigDecimal](lb) shouldBe Success(Long.MaxValue)
        Injection.invert[Long, BigDecimal](ob) shouldBe 'failure
      }
    }

    describe("Injection[Double, BigDecimal]") {
      it("should convert Double to BigDecimal") {
        Injection[Double, BigDecimal](Double.MaxValue) shouldBe db
      }

      it("should invert BigDecimal to Double") {
        Injection.invert[Double, BigDecimal](db) shouldBe Success(Double.MaxValue)
        Injection.invert[Double, BigDecimal](ob) shouldBe 'failure
      }
    }

    describe("Injection[BigDecimal, String]") {
      it("should convert BigDecimal to String") {
        Injection[BigDecimal, String](ib) shouldBe Int.MaxValue.toString
      }

      it("should invert String to BigInteger") {
        Injection.invert[BigDecimal, String](Int.MaxValue.toString) shouldBe
          Success(ib)
        Injection.invert[BigDecimal, String]("booo") shouldBe 'failure
      }
    }

    describe("Injection[BigInt, BigDecimal]") {
      it("should convert BigInt to BigDecimal") {
        Injection[BigInt, BigDecimal](BigInt(Int.MaxValue)) shouldBe ib
      }

      it("should invert BigDecimal to BigInt") {
        Injection.invert[BigInt, BigDecimal](ib) shouldBe
          Success(BigInt(Int.MaxValue))
        Injection.invert[BigInt, BigDecimal](BigDecimal(0.5d)) shouldBe 'failure
      }
    }
  }
}
