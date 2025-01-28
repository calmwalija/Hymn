package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import net.techandgraphics.hymn.data.local.BaseDao
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity

@Dao
interface TimeSpentDao : BaseDao<TimeSpentEntity> {

  @Query("SELECT SUM(timeSpent) as timeSpent, number, id, lang from time_spent group by number")
  suspend fun toExport(): List<TimeSpentEntity>

  @Query("SELECT COUNT(*) from time_spent WHERE number=:number AND  lang=:lang AND timeSpent=:timeSpent")
  suspend fun getCount(number: Int, lang: String, timeSpent: Long): Int
}
