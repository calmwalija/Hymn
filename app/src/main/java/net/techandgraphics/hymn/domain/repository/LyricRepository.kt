package net.techandgraphics.hymn.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.Lyric

interface LyricRepository {
  suspend fun insert(lyric: List<Lyric>)
  suspend fun update(lyric: Lyric)
  suspend fun count(): Int
  suspend fun clearFavorite()
  suspend fun reset()
  fun observeLyrics(query: String = ""): Flow<PagingData<Lyric>>
  fun observeCategories(): Flow<List<Discover>>
  fun observeTopPickCategories(): Flow<List<Lyric>>
  fun observeRecentLyrics(): Flow<List<Lyric>>
  fun getLyricsById(number: Int): Flow<List<Lyric>>
  fun getLyricsByCategory(id: Int): Flow<List<Lyric>>
  fun observeFavoriteLyrics(): Flow<List<Lyric>>
  fun findLyricById(id: Int): Flow<Lyric>

  val queryRandom: Flow<List<Lyric>>
}