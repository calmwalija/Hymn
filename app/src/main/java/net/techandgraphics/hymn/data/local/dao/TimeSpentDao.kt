package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.analytics.HymnStatRow
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity

@Dao
interface TimeSpentDao : BaseDao<TimeSpentEntity> {

  @Query("SELECT SUM(timeSpent) as timeSpent, number, id, lang, MAX(createdAt) as createdAt from time_spent group by number")
  suspend fun toExport(): List<TimeSpentEntity>

  @Query("SELECT COUNT(*) from time_spent WHERE number=:number AND  lang=:lang AND timeSpent=:timeSpent")
  suspend fun getCount(number: Int, lang: String, timeSpent: Long): Int

  @Query(
    """
    SELECT t.number AS number, t.lang AS lang, SUM(t.timeSpent) AS metric,
      COALESCE((SELECT l.title FROM lyric l WHERE l.number = t.number AND l.lang = t.lang LIMIT 1), '') AS title,
      COALESCE((SELECT l.categoryName FROM lyric l WHERE l.number = t.number AND l.lang = t.lang LIMIT 1), '') AS categoryName
    FROM time_spent t
    WHERE t.lang = :lang
      AND (:fromMs = 0 OR t.createdAt >= :fromMs)
      AND (:toMs = 0 OR t.createdAt < :toMs)
      AND (:fromMs = 0 OR t.createdAt > 0)
    GROUP BY t.number, t.lang
    ORDER BY metric DESC
    LIMIT :limit
    """
  )
  suspend fun topByTime(lang: String, fromMs: Long, toMs: Long, limit: Int): List<HymnStatRow>

  @Query(
    """
    SELECT COALESCE(SUM(timeSpent), 0) FROM time_spent
    WHERE lang = :lang
      AND (:fromMs = 0 OR createdAt >= :fromMs)
      AND (:toMs = 0 OR createdAt < :toMs)
      AND (:fromMs = 0 OR createdAt > 0)
    """
  )
  suspend fun totalTimeMs(lang: String, fromMs: Long, toMs: Long): Long

  @Query("DELETE FROM time_spent")
  suspend fun clearAll()
}
