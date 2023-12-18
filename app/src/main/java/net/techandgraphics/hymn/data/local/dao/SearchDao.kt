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
  suspend fun upsert(searchEntities: List<SearchEntity>)

  @Delete
  suspend fun delete(searchEntity: SearchEntity)

  @Query("DELETE  FROM  Search")
  suspend fun clear()

  @Query("SELECT * FROM Search WHERE lang=:version ORDER BY id DESC LIMIT 3")
  fun query(version: String): Flow<List<SearchEntity>>
}
