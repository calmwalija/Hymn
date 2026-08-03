package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.analytics.CategoryStatRow
import net.techandgraphics.hymn.data.local.analytics.HymnStatRow
import net.techandgraphics.hymn.data.local.analytics.LangSplitRow
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

@Dao
interface TimestampDao : BaseDao<TimestampEntity> {

  @Query("SELECT MAX(timestamp) AS timestamp, number, lang, id  FROM timestamp group by number")
  suspend fun toExport(): List<TimestampEntity>

  @Query("SELECT COUNT(*) FROM timestamp WHERE number=:number AND lang=:lang AND timestamp=:timestamp")
  suspend fun ifExist(lang: String, number: Int, timestamp: Long): Int

  @Query(
    """
    SELECT t.number AS number, t.lang AS lang, COUNT(*) AS metric,
      COALESCE((SELECT l.title FROM lyric l WHERE l.number = t.number AND l.lang = t.lang LIMIT 1), '') AS title,
      COALESCE((SELECT l.categoryName FROM lyric l WHERE l.number = t.number AND l.lang = t.lang LIMIT 1), '') AS categoryName
    FROM timestamp t
    WHERE t.lang = :lang AND (:fromMs = 0 OR t.timestamp >= :fromMs) AND (:toMs = 0 OR t.timestamp < :toMs)
    GROUP BY t.number, t.lang
    ORDER BY metric DESC
    LIMIT :limit
    """
  )
  suspend fun mostVisited(lang: String, fromMs: Long, toMs: Long, limit: Int): List<HymnStatRow>

  @Query(
    """
    SELECT COUNT(*) FROM timestamp
    WHERE lang = :lang AND (:fromMs = 0 OR timestamp >= :fromMs) AND (:toMs = 0 OR timestamp < :toMs)
    """
  )
  suspend fun totalVisits(lang: String, fromMs: Long, toMs: Long): Long

  @Query(
    """
    SELECT COUNT(DISTINCT number) FROM timestamp
    WHERE lang = :lang AND (:fromMs = 0 OR timestamp >= :fromMs) AND (:toMs = 0 OR timestamp < :toMs)
    """
  )
  suspend fun uniqueHymns(lang: String, fromMs: Long, toMs: Long): Long

  @Query(
    """
    SELECT DISTINCT timestamp FROM timestamp
    WHERE lang = :lang AND (:fromMs = 0 OR timestamp >= :fromMs) AND (:toMs = 0 OR timestamp < :toMs)
    ORDER BY timestamp DESC
    """
  )
  suspend fun visitTimestamps(lang: String, fromMs: Long, toMs: Long): List<Long>

  @Query(
    """
    SELECT COALESCE(l.categoryName, 'Unknown') AS categoryName, COUNT(*) AS visitCount
    FROM timestamp t
    LEFT JOIN lyric l ON l.number = t.number AND l.lang = t.lang
    WHERE t.lang = :lang AND (:fromMs = 0 OR t.timestamp >= :fromMs) AND (:toMs = 0 OR t.timestamp < :toMs)
    GROUP BY categoryName
    ORDER BY visitCount DESC
    LIMIT :limit
    """
  )
  suspend fun topCategories(lang: String, fromMs: Long, toMs: Long, limit: Int): List<CategoryStatRow>

  @Query(
    """
    SELECT lang AS lang, COUNT(*) AS visitCount FROM timestamp
    WHERE (:fromMs = 0 OR timestamp >= :fromMs) AND (:toMs = 0 OR timestamp < :toMs)
    GROUP BY lang
    """
  )
  suspend fun languageSplit(fromMs: Long, toMs: Long): List<LangSplitRow>

  @Query(
    """
    SELECT MAX(timestamp) AS timestamp, number, lang, id FROM timestamp
    WHERE lang = :lang
    GROUP BY number, lang
    ORDER BY timestamp DESC
    LIMIT :limit
    """
  )
  suspend fun recentVisits(lang: String, limit: Int): List<TimestampEntity>
}
