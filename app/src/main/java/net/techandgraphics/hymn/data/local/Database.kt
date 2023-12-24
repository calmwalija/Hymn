package net.techandgraphics.hymn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.techandgraphics.hymn.data.local.dao.CategoryDao
import net.techandgraphics.hymn.data.local.dao.LyricDao
import net.techandgraphics.hymn.data.local.dao.OtherDao
import net.techandgraphics.hymn.data.local.dao.SearchDao
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.OtherEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity

@Database(entities = [LyricEntity::class, SearchEntity::class, OtherEntity::class], version = 4, exportSchema = false)
abstract class Database : RoomDatabase() {
  abstract val lyricDao: LyricDao
  abstract val searchDao: SearchDao
  abstract val categoryDao: CategoryDao
  abstract val otherDao: OtherDao
}
