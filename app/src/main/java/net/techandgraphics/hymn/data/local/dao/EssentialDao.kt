package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.EssentialEntity

@Dao
interface EssentialDao {

  @Upsert
  suspend fun upsert(lyric: List<EssentialEntity>)

  @Query("DELETE  FROM other")
  suspend fun clearAll()

  @Query("SELECT * FROM other WHERE lang=:version ORDER BY resourceId ")
  suspend fun query(version: String = Lang.EN.lowercase()): List<EssentialEntity>
}
