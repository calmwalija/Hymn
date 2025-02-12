package net.techandgraphics.hymn.data.local

enum class Translation : ToLowerCase {
  EN, CH;

  override fun lowercase() = when (this) {
    EN -> EN.name.lowercase()
    CH -> CH.name.lowercase()
  }
}

fun String.toLang() = when (this) {
  Translation.EN.name.lowercase() -> Translation.EN
  Translation.CH.name.lowercase() -> Translation.CH
  else -> Translation.EN
}

interface ToLowerCase {
  fun lowercase(): String
}
