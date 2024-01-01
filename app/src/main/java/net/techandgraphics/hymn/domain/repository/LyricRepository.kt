package net.techandgraphics.hymn.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Lyric

interface LyricRepository {
  fun query(query: String): Flow<PagingData<Lyric>>
  fun queryByCategory(id: Int): Flow<List<Lyric>>
  fun diveInto(): Flow<List<Lyric>>
  fun queryById(lyricId: Int): Flow<List<Lyric>>
  fun favorites(): Flow<List<Lyric>>
  suspend fun upsert(lyric: List<Lyric>)
  suspend fun queryByNumber(number: Int): List<Lyric>
  suspend fun queryId(): Int?
  suspend fun favorite(favorite: Boolean, number: Int)
  suspend fun read(number: Int, timestamp: Long)
  suspend fun backup(): List<Lyric>
}
