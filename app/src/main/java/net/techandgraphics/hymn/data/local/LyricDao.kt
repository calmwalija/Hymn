package net.techandgraphics.hymn.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.Lyric

@Dao
interface LyricDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(lyric: List<Lyric>)

  @Update
  suspend fun update(lyric: Lyric)

  @Query("SELECT COUNT(*) FROM lyric")
  suspend fun count(): Int

  @Query(
    """ SELECT * FROM lyric WHERE
               (content LIKE'%' || :query || '%'  OR
               title LIKE'%' || :query || '%'  OR
               number LIKE'%' || :query || '%'  OR
               categoryName  LIKE'%' || :query || '%' )
              AND  lang=:version GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT)"""
  )
  fun observeLyrics(query: String = "", version: String): PagingSource<Int, Lyric>

  @Query("SELECT COUNT(DISTINCT(number)) as count, * FROM lyric WHERE lang=:version  GROUP BY categoryName ORDER BY categoryName ASC")
  fun observeCategories(version: String): Flow<List<Discover>>

  @Query("SELECT * FROM lyric WHERE  topPickHit > 0 AND lang=:version GROUP BY categoryName ORDER BY topPickHit DESC LIMIT 6")
  fun observeTopPickCategories(version: String): Flow<List<Lyric>>

  @Query("SELECT * FROM lyric  WHERE topPickHit > 0 AND lang=:version GROUP BY number ORDER BY timestamp DESC , lyricId LIMIT 6")
  fun observeRecentLyrics(version: String): Flow<List<Lyric>>

  @Query("SELECT * FROM lyric WHERE number=:number AND lang=:version ORDER BY lyricId ASC")
  fun getLyricsById(number: Int, version: String): Flow<List<Lyric>>

  @Query("SELECT * FROM lyric WHERE categoryId=:id AND lang=:version GROUP BY number ORDER BY lyricId ASC")
  fun getLyricsByCategory(id: Int, version: String): Flow<List<Lyric>>

  @Query("SELECT * FROM lyric  WHERE favorite = 1 ORDER BY CAST(number AS INT)")
  fun observeFavoriteLyrics(): Flow<List<Lyric>>

  @Query("UPDATE lyric SET favorite = 0 ")
  suspend fun clearFavorite()

  @Query("SELECT * FROM lyric WHERE lyricId =:id AND lang=:version GROUP BY number ORDER BY lyricId")
  fun findLyricById(id: Int, version: String): Flow<Lyric>

  @Query("UPDATE lyric SET topPickHit = 0")
  suspend fun reset()

  @Query("SELECT * FROM lyric WHERE lang=:version  ORDER BY RANDOM() LIMIT 5")
  fun queryRandom(version: String): Flow<List<Lyric>>
}
