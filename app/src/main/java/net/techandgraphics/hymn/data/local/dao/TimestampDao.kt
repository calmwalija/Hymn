package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

@Dao
interface TimestampDao : BaseDao<TimestampEntity> {

  @Query("SELECT * FROM timestamp")
  suspend fun query(): List<TimestampEntity>
}
