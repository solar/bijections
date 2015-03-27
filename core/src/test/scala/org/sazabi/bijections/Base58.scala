package org.sazabi.bijections

import scala.util.Success

import com.twitter.bijection._
import org.scalatest._

class Base58Spec extends FlatSpec with Matchers with Base58Bijections {
  val valid = "17wjHPRxwP5QYu2CJsRqNP6gbre7Uig36N"
  val base58 = Base58String(valid)

  val bytes: Array[Byte] = Array(0, 76, 42, -125, 39, 65, 49, -98, -123,
    18, -47, 93, -73, -125, -84, -113, -65, 121, 90, 104, 69, -4, 8, 106,
    7)

  val invalid = "43290adfqOweoalalsdjfaqeowaur9324I"

  "Bijection[Array[Byte], Base58String]" should
    "convert Array[Byte] to Base58String" in {
      Bijection[Array[Byte], Base58String](bytes) shouldBe base58
    }

  it should "invert Base58String to Array[Byte]" in {
    Bijection.invert[Array[Byte], Base58String](base58) shouldBe bytes
  }

  "Injection[Base58String, String]" should
    "convert Base58String to String" in {
      Injection[Base58String, String](base58) shouldBe valid
    }

  it should "invert valid base58 String to Success(Base58String)" in {
    Injection.invert[Base58String, String](valid) shouldBe Success(base58)
  }

  it should "invert invalid base58 String to Failure" in {
    Injection.invert[Base58String, String](invalid) shouldBe 'failure
  }
}
