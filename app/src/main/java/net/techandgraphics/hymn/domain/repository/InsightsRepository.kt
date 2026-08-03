package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.domain.model.CategoryAffinity
import net.techandgraphics.hymn.domain.model.HymnStat
import net.techandgraphics.hymn.domain.model.InsightsSummary
import net.techandgraphics.hymn.domain.model.YearInHymnsReport

interface InsightsRepository {
  suspend fun summary(fromMs: Long = 0L, toMs: Long = 0L): InsightsSummary
  suspend fun mostVisited(limit: Int = 10, fromMs: Long = 0L, toMs: Long = 0L): List<HymnStat>
  suspend fun topByTime(limit: Int = 10, fromMs: Long = 0L, toMs: Long = 0L): List<HymnStat>
  suspend fun topCategories(limit: Int = 5, fromMs: Long = 0L, toMs: Long = 0L): List<CategoryAffinity>
  suspend fun languageSplit(fromMs: Long = 0L, toMs: Long = 0L): Map<String, Long>
  suspend fun yearReport(year: Int): YearInHymnsReport
}
