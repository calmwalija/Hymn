package net.techandgraphics.hymn.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.EssentialEntity

@Dao
interface EssentialDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(item: List<EssentialEntity>)

  @Query("DELETE  FROM  other ")
  suspend fun clear()

  @Query("SELECT * FROM other WHERE lang=:version ORDER BY resourceId ")
  fun query(version: String): Flow<List<EssentialEntity>>
}
