package net.techandgraphics.hymn.domain.model

data class Lyric(
  val lyricId: Int,
  val categoryId: Int,
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val title: String,
  val lang: String,
  val timestamp: Long,
  val favorite: Boolean,

//  val topPick: String,
//  val justAdded: Boolean,
//  val forTheService: Boolean = false,
//  val ftsSuggestion: Boolean = false,
//  val millsAdded: Long = System.currentTimeMillis(),
)
