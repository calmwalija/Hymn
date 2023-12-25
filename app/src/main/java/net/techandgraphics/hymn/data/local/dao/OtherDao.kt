package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import net.techandgraphics.hymn.data.local.entities.OtherEntity

@Dao
interface OtherDao {

  @Upsert
  suspend fun upsert(data: List<OtherEntity>)

  @Query("SELECT * FROM other WHERE lang=:lang ORDER BY resourceId")
  suspend fun query(lang: String): List<OtherEntity>

  @Query("DELETE FROM other")
  suspend fun clearAll()
}
