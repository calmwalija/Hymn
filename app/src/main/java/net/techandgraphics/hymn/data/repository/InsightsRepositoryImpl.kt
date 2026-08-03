package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.data.local.analytics.HymnStatRow
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.CategoryAffinity
import net.techandgraphics.hymn.domain.model.HymnStat
import net.techandgraphics.hymn.domain.model.InsightsSummary
import net.techandgraphics.hymn.domain.model.YearInHymnsReport
import net.techandgraphics.hymn.domain.repository.InsightsRepository
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class InsightsRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: DataStorePrefs,
) : InsightsRepository {

  private val timestampDao = database.timestampDao
  private val timeSpentDao = database.timeSpentDao

  private suspend fun lang() =
    prefs.get(prefs.translationKey, Translation.EN.lowercase())

  override suspend fun summary(fromMs: Long, toMs: Long): InsightsSummary {
    val language = lang()
    val timestamps = timestampDao.visitTimestamps(language, fromMs, toMs)
    val dayKeys = timestamps.map { dayKey(it) }.toSet()
    return InsightsSummary(
      totalVisits = timestampDao.totalVisits(language, fromMs, toMs),
      uniqueHymns = timestampDao.uniqueHymns(language, fromMs, toMs),
      totalTimeMs = timeSpentDao.totalTimeMs(language, fromMs, toMs),
      activeDays = dayKeys.size,
      currentStreak = currentStreak(dayKeys),
    )
  }

  override suspend fun mostVisited(limit: Int, fromMs: Long, toMs: Long): List<HymnStat> {
    return timestampDao.mostVisited(lang(), fromMs, toMs, limit).map { it.toVisitStat() }
  }

  override suspend fun topByTime(limit: Int, fromMs: Long, toMs: Long): List<HymnStat> {
    return timeSpentDao.topByTime(lang(), fromMs, toMs, limit).map { it.toTimeStat() }
  }

  override suspend fun topCategories(
    limit: Int,
    fromMs: Long,
    toMs: Long,
  ): List<CategoryAffinity> {
    return timestampDao.topCategories(lang(), fromMs, toMs, limit).map {
      CategoryAffinity(it.categoryName, it.visitCount)
    }
  }

  override suspend fun languageSplit(fromMs: Long, toMs: Long): Map<String, Long> {
    return timestampDao.languageSplit(fromMs, toMs).associate { it.lang to it.visitCount }
  }

  override suspend fun yearReport(year: Int): YearInHymnsReport {
    val fromMs = yearStartMs(year)
    val toMs = yearStartMs(year + 1)
    return YearInHymnsReport(
      year = year,
      summary = summary(fromMs, toMs),
      topVisited = mostVisited(5, fromMs, toMs),
      topByTime = topByTime(5, fromMs, toMs),
      topCategories = topCategories(5, fromMs, toMs),
      languageSplit = languageSplit(fromMs, toMs),
    )
  }

  private fun HymnStatRow.toVisitStat() = HymnStat(
    number = number,
    lang = lang,
    title = title,
    categoryName = categoryName,
    visitCount = metric,
  )

  private fun HymnStatRow.toTimeStat() = HymnStat(
    number = number,
    lang = lang,
    title = title,
    categoryName = categoryName,
    totalTimeMs = metric,
  )

  private fun yearStartMs(year: Int): Long {
    val calendar = Calendar.getInstance().apply {
      clear()
      set(Calendar.YEAR, year)
      set(Calendar.MONTH, Calendar.JANUARY)
      set(Calendar.DAY_OF_MONTH, 1)
    }
    return calendar.timeInMillis
  }

  private fun dayKey(millis: Long): Long = TimeUnit.MILLISECONDS.toDays(millis)

  private fun currentStreak(dayKeys: Set<Long>): Int {
    if (dayKeys.isEmpty()) return 0
    var day = dayKey(System.currentTimeMillis())
    if (day !in dayKeys) {
      day -= 1
      if (day !in dayKeys) return 0
    }
    var streak = 0
    while (day in dayKeys) {
      streak++
      day--
    }
    return streak
  }
}
