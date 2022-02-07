package net.techandgraphics.hymn.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.fragments.main.MainFragment.Companion.SortBy

@Dao
interface LyricDao {

    @Insert
    suspend fun insert(lyric: List<Lyric>)

    @Update
    suspend fun update(lyric: Lyric)

    @Query("SELECT COUNT(*) FROM lyric")
    suspend fun count(): Int

    @Query(
        """ SELECT * FROM lyric WHERE  
               content LIKE'%' || :query || '%'  OR 
               title LIKE'%' || :query || '%'  OR 
               number LIKE'%' || :query || '%'  OR 
               categoryName  LIKE'%' || :query || '%'  
                GROUP BY number ORDER BY lyricId ASC"""
    )
    fun observeLyrics(query: String = ""): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY categoryName ORDER BY categoryName ASC")
    fun observeCategories(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric WHERE  topPickHit > 0 GROUP BY categoryName ORDER BY topPickHit DESC LIMIT 6")
    fun observeTopPickCategories(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric  WHERE topPickHit > 0 GROUP BY number ORDER BY timestamp DESC , lyricId LIMIT 6")
    fun observeRecentLyrics(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric WHERE number=:number ORDER BY lyricId ASC")
    fun getLyricsById(number: Int): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric WHERE categoryId=:id GROUP BY number ORDER BY lyricId ASC")
    fun getLyricsByCategory(id: Int): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric  WHERE favorite = 1 ORDER BY number")
    fun observeFavoriteLyrics(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY number  ORDER BY number ")
    fun sortByNumber(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY number  ORDER BY title ")
    fun sortByName(): Flow<List<Lyric>>

    @Query("SELECT * FROM lyric GROUP BY number  ORDER BY categoryName ")
    fun sortByCategory(): Flow<List<Lyric>>

    @Query("UPDATE lyric SET favorite = 0 ")
    suspend fun clearFavorite()

    @Query("SELECT * FROM lyric WHERE lyricId =:id GROUP BY number ORDER BY lyricId")
    fun findLyricById(id: Int): Flow<Lyric>

    fun observeSortBy(sortBy: String): Flow<List<Lyric>>? {
        return when (sortBy) {
            SortBy.NUMBER.name -> sortByNumber()
            SortBy.NAME.name -> sortByName()
            SortBy.CATEGORY.name -> sortByCategory()
            else -> null
        }
    }

}