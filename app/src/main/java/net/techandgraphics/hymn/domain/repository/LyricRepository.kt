package net.techandgraphics.hymn.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity

interface LyricRepository {
  suspend fun insert(lyric: List<LyricEntity>)
  suspend fun update(lyric: LyricEntity)
  suspend fun count(): Int
  suspend fun clearFavorite()
  suspend fun reset()
  fun observeLyrics(query: String = ""): Flow<PagingData<LyricEntity>>
  fun observeCategories(): Flow<List<Discover>>
  fun observeRecentLyrics(): Flow<List<LyricEntity>>
  fun getLyricsById(number: Int): Flow<List<LyricEntity>>
  fun getLyricsByCategory(id: Int): Flow<List<LyricEntity>>
  fun findLyricById(id: Int): Flow<LyricEntity>

  val queryRandom: Flow<List<LyricEntity>>
  val mostVisited: Flow<List<LyricEntity>>
  val favorite: Flow<List<LyricEntity>>
}
