package net.techandgraphics.hymn.data.local

enum class Lang : ToLowerCase {
  EN, CH;

  override fun lowercase() = when (this) {
    EN -> EN.name.lowercase()
    CH -> CH.name.lowercase()
  }
}

interface ToLowerCase {
  fun lowercase(): String
}
