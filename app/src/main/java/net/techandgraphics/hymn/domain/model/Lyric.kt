package net.techandgraphics.hymn.domain.model

data class Lyric(
  val lyricId: Int,
  val categoryId: Int,
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val favorite: Boolean,
  val title: String,
  val lang: String,
  val timestamp: Long,
)
