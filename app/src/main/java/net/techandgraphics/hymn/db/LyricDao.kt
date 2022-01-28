package net.techandgraphics.hymn.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.models.Lyric

@Dao
interface LyricDao {

    @Insert
    suspend fun insert(lyric: List<Lyric>)

    @Update
    suspend fun update(lyric: Lyric)

    @Update
    suspend fun updateList(lyric: List<Lyric>)

    @Query(
        """ SELECT * FROM lyric WHERE  
               content LIKE'%' || :query || '%'  OR 
               number LIKE'%' || :query || '%'  OR 
               categoryName  LIKE'%' || :query || '%'  
                GROUP BY number ORDER BY lyricId ASC"""
    )
    fun observeLyrics(query: String = ""): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY categoryName ORDER BY categoryName ASC")
    fun observeCategories(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY categoryName ORDER BY topPickHit DESC LIMIT 4")
    fun observeTopPickCategories(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY number ORDER BY timestamp DESC , lyricId LIMIT 10")
    fun observeRecentLyrics(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric WHERE number=:number ORDER BY lyricId ASC")
    fun getLyricsById(number: Int): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric WHERE categoryId=:id GROUP BY number ORDER BY lyricId ASC")
    fun getLyricsByCategory(id: Int): Flow<List<Lyric>>

}