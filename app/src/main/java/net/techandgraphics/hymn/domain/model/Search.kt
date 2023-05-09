package net.techandgraphics.hymn.domain.model

data class Search(
  val id: Long = System.currentTimeMillis(),
  val query: String,
  val tag: String,
  val lang: String = "en"
)
