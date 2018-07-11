package tutorial.webapp.services.js

object Json {
  trait Json[T] {
    def stringify(t: T): String
    def parse(s: String): T
  }

  trait EncodeJson[T, J] {
    def encode(t: T): Json[J]
  }

  trait DecodeJson[T, J] {
    def decode(j: Json[J]): T
  }
}

