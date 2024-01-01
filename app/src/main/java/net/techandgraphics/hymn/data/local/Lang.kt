package net.techandgraphics.hymn.data.local

enum class Lang : ToLowerCase {
  EN, CH;

  override fun lowercase() = when (this) {
    EN -> EN.name.lowercase()
    CH -> CH.name.lowercase()
  }
}

fun String.toLang() = when (this) {
  Lang.EN.name.lowercase() -> Lang.EN
  Lang.CH.name.lowercase() -> Lang.CH
  else -> Lang.EN
}

interface ToLowerCase {
  fun lowercase(): String
//  fun fromString(value: String): Lang
}
