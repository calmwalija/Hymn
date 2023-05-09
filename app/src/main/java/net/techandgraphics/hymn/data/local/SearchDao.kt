package net.techandgraphics.hymn.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.SearchEntity

@Dao
interface SearchDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(searchEntities: List<SearchEntity>)

  @Delete
  suspend fun delete(searchEntity: SearchEntity)

  @Query("DELETE  FROM  search")
  suspend fun clear()

  @Query("SELECT * FROM search WHERE lang=:version ORDER BY id DESC LIMIT 12")
  fun query(version: String): Flow<List<SearchEntity>>
}
