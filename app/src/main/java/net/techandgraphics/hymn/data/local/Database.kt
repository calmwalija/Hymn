package net.techandgraphics.hymn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.techandgraphics.hymn.data.local.dao.CategoryDao
import net.techandgraphics.hymn.data.local.dao.LyricDao
import net.techandgraphics.hymn.data.local.dao.OtherDao
import net.techandgraphics.hymn.data.local.dao.SearchDao
import net.techandgraphics.hymn.data.local.dao.TimeSpentDao
import net.techandgraphics.hymn.data.local.dao.TimestampDao
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.OtherEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

@Database(
  entities = [LyricEntity::class, SearchEntity::class, OtherEntity::class, TimestampEntity::class, TimeSpentEntity::class],
  version = 5,
  exportSchema = false
)
abstract class Database : RoomDatabase() {
  abstract val lyricDao: LyricDao
  abstract val searchDao: SearchDao
  abstract val categoryDao: CategoryDao
  abstract val otherDao: OtherDao
  abstract val timestampDao: TimestampDao
  abstract val timeSpentDao: TimeSpentDao
}
