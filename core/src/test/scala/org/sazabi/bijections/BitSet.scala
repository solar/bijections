package org.sazabi.bijections

import scala.collection.immutable.BitSet

import com.twitter.bijection._
import scalaprops._, Property.forAll
import scalaz.std.string._

object BitSetTest extends Scalaprops with BitSetBijections {
  implicit val maskGen = for {
    i <- Gen.choose(1, 3)
    n <- Gen.sequenceNList(i, Gen[Long])
  } yield n.toArray

  val bij = implicitly[Bijection[Array[Long], BitSet]]

  val bijectionBitSet = {
    val p1 = forAll { (mask: Array[Long]) =>
      BitSet.fromBitMask(mask) == bij(mask)
    }.toProperties("1. Array[Byte] -> BitSet")

    val p2 = forAll { (mask: Array[Long]) =>
      bij.invert(BitSet.fromBitMask(mask)).toSeq == mask.toSeq
    }.toProperties("2. BitSet -> Array[Byte]")

    Properties.list(p1, p2)
  }
}
