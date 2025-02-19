package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.SearchEntity

@Dao
interface SearchDao {

  @Upsert
  suspend fun upsert(data: List<SearchEntity>)

  @Delete
  suspend fun delete(data: SearchEntity)

  @Query("DELETE FROM Search")
  suspend fun clear()

  @Query("SELECT * FROM Search WHERE lang=:lang ORDER BY id DESC LIMIT 9")
  fun query(lang: String): Flow<List<SearchEntity>>

  @Query("SELECT * FROM Search")
  suspend fun toExport(): List<SearchEntity>
}
