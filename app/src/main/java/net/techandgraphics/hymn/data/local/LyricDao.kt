package net.techandgraphics.hymn.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity

@Dao
interface LyricDao {

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insert(lyric: List<LyricEntity>)

  @Upsert
  suspend fun upsert(lyric: List<LyricEntity>)

  @Update
  suspend fun update(lyric: LyricEntity)

  @Query("DELETE  FROM lyric")
  suspend fun delete()

  @Query("SELECT COUNT(*) FROM lyric WHERE  lang=:version")
  suspend fun count(version: String): Int?

  @Query("SELECT * FROM lyric  WHERE  lang=:version GROUP BY number ORDER BY timestamp DESC LIMIT 5")
  fun theHymn(version: String): Flow<List<LyricEntity>>

  @Query(
    """
      SELECT * FROM lyric WHERE  (lang=:version AND justAdded = 1 AND millsAdded > 0)
      GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT) LIMIT 6
  """
  )
  fun justAdded(version: String): Flow<List<LyricEntity>>

  @Query("SELECT number FROM lyric WHERE  lang=:version GROUP BY number ORDER BY number DESC LIMIT 1")
  suspend fun lastInsertedHymn(version: String): Int?

  @Query(
    """ SELECT * FROM lyric WHERE
               (content LIKE'%' || :query || '%'  OR
               title LIKE'%' || :query || '%'  OR
               number LIKE'%' || :query || '%'  OR
               categoryName  LIKE'%' || :query || '%' )
              AND  lang=:version GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT)"""
  )
  fun observeLyrics(query: String = "", version: String): PagingSource<Int, LyricEntity>

  @Query("SELECT COUNT(DISTINCT(number)) || '-' || SUM(favorite)  as count, *  FROM lyric WHERE lang=:version  GROUP BY categoryName ORDER BY categoryName ASC")
  fun observeCategories(version: String): Flow<List<Discover>>

  @Query("SELECT * FROM lyric WHERE number=:number AND lang=:version ORDER BY lyricId ASC")
  fun getLyricsById(number: Int, version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE categoryId=:id AND lang=:version GROUP BY number ORDER BY lyricId ASC")
  fun getLyricsByCategory(id: Int, version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric  WHERE favorite = 1 AND lang=:version ORDER BY CAST(number AS INT)")
  fun observeFavoriteLyrics(version: String): Flow<List<LyricEntity>>

  @Query("UPDATE lyric SET favorite = 0 ")
  suspend fun clearFavorite()

  @Query("SELECT * FROM lyric WHERE lyricId =:id AND lang=:version GROUP BY number ORDER BY lyricId")
  fun findLyricById(id: Int, version: String): Flow<LyricEntity>

  @Query("UPDATE lyric SET topPickHit = 0")
  suspend fun reset()

  @Query("SELECT * FROM lyric WHERE lang=:version  ORDER BY RANDOM() LIMIT 5")
  fun queryRandom(version: String): Flow<List<LyricEntity>>

  @Query("SELECT lyricId FROM lyric ORDER BY lyricId DESC LIMIT 1")
  suspend fun lastInsertedId(): Long?

  @Query("SELECT COUNT(DISTINCT(number)) as count, * FROM lyric WHERE lang=:version  GROUP BY categoryName LIMIT :limit, 4")
  fun featuredHymn(version: String, limit: Int): Flow<List<Discover>>

  @Query("SELECT COUNT(*) FROM lyric WHERE lang=:version  GROUP BY categoryName ")
  suspend fun categoryCount(version: String): List<Int>

  @Query("DELETE  FROM lyric")
  suspend fun clearLyric()

  @Query("DELETE  FROM Search")
  suspend fun clearSearch()

  @Query("DELETE  FROM Other")
  suspend fun clearOther()

  @Query("SELECT * FROM lyric WHERE lyricId >:number ORDER BY lyricId ASC")
  suspend fun getLyricsByIdRange(number: Long): List<LyricEntity>

  @Query("SELECT * FROM lyric WHERE lyricId >=:number AND lang=:lang ORDER BY lyricId ASC")
  fun getLyricsByIdRangeLang(number: Long, lang: String): Flow<List<LyricEntity>>
}
