package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

@Dao
interface TimestampDao : BaseDao<TimestampEntity> {

  @Query("SELECT MAX(timestamp) AS timestamp, number, lang, id  FROM timestamp group by number")
  suspend fun toExport(): List<TimestampEntity>

  @Query("SELECT COUNT(*) FROM timestamp WHERE number=:number AND lang=:lang AND timestamp=:timestamp")
  suspend fun ifExist(lang: String, number: Int, timestamp: Long): Int
}
