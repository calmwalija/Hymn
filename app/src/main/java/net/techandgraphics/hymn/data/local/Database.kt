package net.techandgraphics.hymn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.model.Search

@Database(entities = [Lyric::class, Search::class, Other::class], version = 3)
abstract class Database : RoomDatabase() {
  abstract val lyricDao: LyricDao
  abstract val searchDao: SearchDao
  abstract val otherDao: OtherDao
}
