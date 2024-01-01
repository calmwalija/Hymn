package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.join.CategoryEmbedded

@Dao
interface CategoryDao : BaseDao<LyricEntity> {

  @Transaction
  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:lang  GROUP BY categoryName ORDER BY categoryName ASC")
  fun query(lang: String): Flow<List<CategoryEmbedded>>

  @Transaction
  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:lang  GROUP BY categoryName ORDER BY RANDOM() LIMIT 4")
  suspend fun spotlight(lang: String): List<CategoryEmbedded>

  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:lang AND categoryId=:id  GROUP BY categoryName ORDER BY categoryName ASC")
  fun queryById(id: Int, lang: String): Flow<List<CategoryEmbedded>>
}
