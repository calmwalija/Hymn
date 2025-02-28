package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Lyric

interface LyricRepository {
  fun query(query: String): Flow<List<Lyric>>
  fun queryByCategory(id: Int): Flow<List<Lyric>>
  fun diveInto(): Flow<List<Lyric>>
  suspend fun toExport(): List<Int>
  fun queryById(lyricId: Int): Flow<List<Lyric>>
  fun favorites(): Flow<List<Lyric>>
  suspend fun upsert(lyric: List<Lyric>)
  suspend fun queryByNumber(number: Int): List<Lyric>
  suspend fun uniquelyCrafted(): List<Lyric>
  suspend fun favorite(favorite: Boolean, number: Int)
  suspend fun read(number: Int, timestamp: Long, lang: String?)
  suspend fun getHymnCount(): Int
  suspend fun emptyStateSuggested(): List<Lyric>
}
