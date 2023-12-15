package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.LyricEntity

@Dao
interface LyricDao {

  @Query(
    """ SELECT * FROM lyric WHERE
               (content LIKE'%' || :query || '%'  OR
               title LIKE'%' || :query || '%'  OR
               number LIKE'%' || :query || '%'  OR
               categoryName  LIKE'%' || :query || '%' )
              AND  lang=:version GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT)"""
  )
  fun query(query: String = "", version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE categoryId=:id AND lang=:version GROUP BY number ORDER BY lyricId ASC")
  fun queryByCategory(id: Int, version: String): Flow<List<LyricEntity>>

  @Upsert
  suspend fun upsert(lyric: List<LyricEntity>)

  @Query("SELECT number FROM lyric WHERE  lang=:version GROUP BY number ORDER BY number DESC LIMIT 1")
  suspend fun lastInsertedId(version: String): Int?

  @Query("SELECT * FROM lyric WHERE number=:number AND lang=:version ORDER BY lyricId ASC")
  fun queryById(number: Int, version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric  WHERE  lang=:version GROUP BY number ORDER BY timestamp DESC LIMIT 5")
  fun theHymn(version: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric  WHERE  lang=:version GROUP BY number ORDER BY RANDOM() LIMIT 5")
  fun forTheService(version: String): Flow<List<LyricEntity>>
}
