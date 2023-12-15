package net.techandgraphics.hymn.domain.model

data class Lyric(
  val lyricId: Int,
  val categoryId: Int,
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val topPick: String,
  val topPickHit: Int,
  val title: String,
  val lang: String,
  val justAdded: Boolean,
  val forTheService: Boolean = false,
  val ftsSuggestion: Boolean = false,
  val timestamp: Long = System.currentTimeMillis(),
  val favorite: Boolean = false,
  val millsAdded: Long = System.currentTimeMillis(),
)
