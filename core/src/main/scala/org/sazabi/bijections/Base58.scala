package org.sazabi.bijections

import com.twitter.bijection.{ AbstractBijection, Bijection, Injection }

import scala.annotation.tailrec
import scala.collection.mutable.StringBuilder

case class Base58String private[bijections] (str: String)

trait Base58Bijections {
  private[this] val Base58Chars =
    "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"

  private[this] val Base58Size = Base58Chars.size

  implicit val base58String2String: Injection[Base58String, String] =
    Injection.buildCatchInvert[Base58String, String](_.str) { str =>
      0 until str.size foreach { i =>
        if (Base58Chars.indexOf(str(i)) == -1) {
          throw InvalidCharacterException(str(i), i)
        }
      }
      Base58String(str)
    }

  implicit val bytes2Base58: Bijection[Array[Byte], Base58String] =
    new AbstractBijection[Array[Byte], Base58String] {
      def apply(bytes: Array[Byte]): Base58String = {
        val bi = BigInt(1, bytes)

        val s = new StringBuilder

        @tailrec
        def append(rest: BigInt) {
          val div = rest / Base58Size
          val mod = rest % Base58Size
          s.insert(0, Base58Chars(mod.intValue))
          if (div > 0) append(div)
        }

        append(bi)

        val zeros = bytes.indexWhere(_ != 0)
        0 until zeros foreach { _ => s.insert(0, Base58Chars(0)) }

        Base58String(s.toString)
      }

      override def invert(b58: Base58String): Array[Byte] = {
        val seq = 0 until b58.str.size map { i =>
          val index = Base58Chars.indexOf(b58.str(i))
          if (index == -1) throw InvalidCharacterException(b58.str(i), i)
          BigInt(index) * BigInt(Base58Size).pow(b58.str.size - 1 - i)
        }

        val bytes = seq.sum.toByteArray

        val offset = if (bytes.size > 2 && bytes(0) == 0 && bytes(1) < 0) 1 else 0;

        val zeros = b58.str.indexWhere(_ != Base58Chars.head)

        val dest = new Array[Byte](bytes.size - offset + zeros)

        Array.copy(bytes, offset, dest, zeros, dest.size - zeros)
        dest
      }
    }

  case class InvalidCharacterException(char: Char, index: Int)
    extends IllegalArgumentException(
      s"An invalid character (${char})) at index ${index}")
}

object base58 extends Base58Bijections
