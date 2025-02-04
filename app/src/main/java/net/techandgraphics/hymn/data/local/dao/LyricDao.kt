package net.techandgraphics.hymn.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.domain.model.Lyric

@Dao
interface LyricDao {

  @Upsert
  suspend fun upsert(data: List<LyricEntity>)

  @Query(
    """ SELECT * FROM lyric WHERE
               (content LIKE'%' || :query || '%'  OR
               title LIKE'%' || :query || '%'  OR
               number LIKE'%' || :query || '%'  OR
               categoryName  LIKE'%' || :query || '%' )
              AND  lang=:lang GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT) ASC"""
  )
  fun query(query: String = "", lang: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE categoryId=:id AND lang=:lang GROUP BY number ORDER BY lyricId ASC")
  fun queryByCategory(id: Int, lang: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE number=:number ORDER BY lyricId ASC")
  suspend fun queryByNumber(number: Int): List<LyricEntity>

  @Query("SELECT * FROM lyric  WHERE  lang=:lang GROUP BY number ORDER BY timestamp DESC LIMIT 5")
  suspend fun diveInto(lang: String): List<LyricEntity>

  @Query("SELECT * FROM lyric WHERE lyricId=:lyricId")
  fun queryById(lyricId: Int): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE lang=:lang GROUP BY categoryName, number ORDER BY RANDOM() LIMIT 10")
  suspend fun uniquelyCrafted(lang: String): List<Lyric>

  @Query("UPDATE lyric SET favorite=:favorite WHERE number=:number AND lang=:lang")
  suspend fun favorite(favorite: Boolean, number: Int, lang: String)

  @Query("UPDATE lyric SET timestamp=:timestamp WHERE number=:number AND lang=:lang")
  suspend fun read(number: Int, timestamp: Long, lang: String)

  @Query("SELECT * FROM lyric WHERE favorite = 1 AND lang=:lang GROUP BY number HAVING MIN(number) ORDER BY CAST(number AS INT) ASC")
  fun favorites(lang: String): Flow<List<LyricEntity>>

  @Query("SELECT * FROM lyric WHERE timestamp > 1 AND lang=:lang GROUP BY number HAVING MIN(number) ORDER BY RANDOM(), CAST(number AS INT) ASC LIMIT 4")
  suspend fun emptyStateSuggested(lang: String): List<LyricEntity>

  @Query("SELECT * FROM lyric WHERE timestamp > 0 OR favorite = 1")
  suspend fun backup(): List<LyricEntity>

  @Query("SELECT number FROM lyric WHERE lang=:lang ORDER BY number DESC LIMIT 1")
  suspend fun getLastHymn(lang: String = Lang.CH.lowercase()): Int

  @Query("SELECT DISTINCT number FROM lyric WHERE favorite = 1 GROUP BY number ")
  suspend fun toExport(): List<Int>
}
