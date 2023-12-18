package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.join.Category

@Dao
interface CategoryDao : BaseDao<LyricEntity> {

  @Transaction
  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:version  GROUP BY categoryName ORDER BY categoryName ASC")
  fun query(version: String): Flow<List<Category>>

  @Transaction
  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:version  GROUP BY categoryName ORDER BY RANDOM() LIMIT 4")
  suspend fun featured(version: String): List<Category>

  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:version AND categoryId=:id  GROUP BY categoryName ORDER BY categoryName ASC")
  fun queryById(id: Int, version: String): Flow<List<Category>>
}
