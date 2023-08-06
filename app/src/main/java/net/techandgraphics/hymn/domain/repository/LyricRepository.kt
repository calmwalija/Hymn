package net.techandgraphics.hymn.domain.repository

import androidx.paging.PagingData
import androidx.work.ListenableWorker
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.Resource
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity

interface LyricRepository {
  suspend fun insert(lyric: List<LyricEntity>)
  suspend fun upsert(lyric: List<LyricEntity>)
  suspend fun update(lyric: LyricEntity)
  suspend fun deleteBecauseHeLives()
  suspend fun lastInsertedHymn(): Int?
  suspend fun clearFavorite()
  suspend fun forTheServiceUpdate(lyric: LyricEntity)
  suspend fun reset()
  fun observeLyrics(query: String = ""): Flow<PagingData<LyricEntity>>
  fun observeCategories(): Flow<List<Discover>>
  fun featuredHymn(limit: Int): Flow<List<Discover>>
  fun getLyricsById(number: Int): Flow<List<LyricEntity>>
  fun getLyricsByCategory(id: Int): Flow<List<LyricEntity>>
  fun findLyricById(id: Int): Flow<LyricEntity>
  suspend fun fetch(status: (Resource<List<LyricEntity>>) -> Unit): ListenableWorker.Result
  fun getLyricsByIdRangeLang(number: Long): Flow<List<LyricEntity>>
  suspend fun count(): Int?
  fun getInverseLyricsById(version: String, id: Int): Flow<List<LyricEntity>>
  suspend fun categoryCount(): List<Int>
  val favorite: Flow<List<LyricEntity>>
  val theHymn: Flow<List<LyricEntity>>
  val justAdded: Flow<List<LyricEntity>>
  val forTheService: Flow<List<LyricEntity>>
  val queryLyrics: Flow<List<LyricEntity>>
  suspend fun queryRandom(): LyricEntity?
}
