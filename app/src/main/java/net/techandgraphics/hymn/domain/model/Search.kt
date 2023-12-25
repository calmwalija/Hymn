package net.techandgraphics.hymn.domain.model

data class Search(
  val query: String,
  val tag: String,
  val lang: String,
  val id: Long,
)
