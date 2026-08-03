package net.techandgraphics.hymn.domain.model

data class HymnStat(
  val number: Int,
  val lang: String,
  val title: String,
  val categoryName: String,
  val visitCount: Long = 0,
  val totalTimeMs: Long = 0,
)

data class CategoryAffinity(
  val categoryName: String,
  val visitCount: Long,
)

data class InsightsSummary(
  val totalVisits: Long,
  val uniqueHymns: Long,
  val totalTimeMs: Long,
  val activeDays: Int,
  val currentStreak: Int,
)

data class YearInHymnsReport(
  val year: Int,
  val summary: InsightsSummary,
  val topVisited: List<HymnStat>,
  val topByTime: List<HymnStat>,
  val topCategories: List<CategoryAffinity>,
  val languageSplit: Map<String, Long>,
)
