package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

@Dao
interface TimestampDao {

  @Upsert
  suspend fun upsert(data: List<TimestampEntity>)
}
