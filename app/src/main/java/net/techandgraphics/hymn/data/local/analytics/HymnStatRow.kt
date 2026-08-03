package net.techandgraphics.hymn.data.local.analytics

/**
 * Lightweight Room projection for ranked hymn analytics.
 */
data class HymnStatRow(
  val number: Int,
  val lang: String,
  val metric: Long,
  val title: String,
  val categoryName: String,
)

data class CategoryStatRow(
  val categoryName: String,
  val visitCount: Long,
)

data class LangSplitRow(
  val lang: String,
  val visitCount: Long,
)
