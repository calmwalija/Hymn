package net.techandgraphics.hymn.db

import androidx.room.Database
import androidx.room.RoomDatabase
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.models.Other
import net.techandgraphics.hymn.models.Search

@Database(entities = [Lyric::class, Search::class, Other::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract val lyricDao: LyricDao
    abstract val searchDao: SearchDao
    abstract val otherDao: OtherDao
}