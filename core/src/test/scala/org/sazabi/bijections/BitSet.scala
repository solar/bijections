package org.sazabi.bijections

import scala.collection.immutable.BitSet

import com.twitter.bijection._
import org.scalatest._

class BitSetSpec extends FlatSpec with Matchers with BitSetBijections {
  val bitset = BitSet(2, 3)
  val mask = Array(12L)

  "Bijection[Array[Long], BitSet]" should "convert Array[Long] to BitSet" in {
    Bijection[Array[Long], BitSet](mask) shouldBe bitset
  }

  it should "invert BitSet to Array[Long]" in {
    Bijection.invert[Array[Long], BitSet](bitset) shouldBe mask
  }
}
