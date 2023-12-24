package net.techandgraphics.hymn.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.LyricEntity

@Dao
interface LyricDao {

  @Query(
    """ SELECT * FROM lyric WHERE
               (content LIKE'%' || :query || '%'  OR
               title LIKE'%' || :query || '%'  OR
               number LIKE'%' || :query || '%'  OR
               categoryName  LIKE'%' || :query || '%' )
              AND  lang=:version GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT) ASC"""
  )
  fun query(query: String = "", version: String = Lang.EN.name): PagingSource<Int, LyricEntity>

  @Query("SELECT * FROM lyric WHERE categoryId=:id AND lang=:version GROUP BY number ORDER BY lyricId ASC")
  fun queryByCategory(id: Int, version: String): Flow<List<LyricEntity>>

  @Upsert
  suspend fun upsert(lyric: List<LyricEntity>)

  @Query("SELECT number FROM lyric WHERE  lang=:version GROUP BY number ORDER BY number DESC LIMIT 1")
  suspend fun lastInsertedId(version: String): Int?

  @Query("SELECT * FROM lyric WHERE number=:number AND lang=:version ORDER BY lyricId ASC")
  suspend fun queryByNumber(number: Int, version: String): List<LyricEntity>

  @Query("SELECT * FROM lyric  WHERE  lang=:version GROUP BY number ORDER BY timestamp DESC LIMIT 2")
  fun theHymn(version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE lang=:version GROUP BY number ORDER BY RANDOM() LIMIT 5")
  fun forTheService(version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE lyricId=:lyricId")
  fun queryById(lyricId: Int): Flow<List<LyricEntity>>

  @Query("SELECT lyricId FROM lyric WHERE lang=:version ORDER BY RANDOM() LIMIT 1")
  suspend fun queryId(version: String = Lang.EN.name): Int?

  @Query("UPDATE lyric SET favorite=:favorite WHERE number=:number AND lang=:version")
  suspend fun favorite(favorite: Boolean, number: Int, version: String = Lang.EN.name)

  @Query("UPDATE lyric SET timestamp=:timestamp, topPickHit=:topPickHit WHERE number=:number AND lang=:version")
  suspend fun read(
    number: Int,
    topPickHit: Int,
    timestamp: Long = System.currentTimeMillis(),
    version: String = Lang.EN.name
  )

  @Query("SELECT * FROM lyric WHERE favorite = 1 AND lang=:version GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT) ASC")
  fun favorites(version: String = Lang.EN.name): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE timestamp > 0 OR favorite = 1")
  suspend fun backup(): List<LyricEntity>
}
