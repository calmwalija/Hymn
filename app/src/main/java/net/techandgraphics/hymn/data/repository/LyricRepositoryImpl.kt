package net.techandgraphics.hymn.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject
import javax.inject.Singleton

const val SIZE = 20

@Singleton
class LyricRepositoryImpl @Inject constructor(
  private val db: Database,
  private val version: String
) : LyricRepository {

  override suspend fun insert(lyric: List<LyricEntity>) {
    db.lyricDao.insert(lyric)
  }

  override suspend fun update(lyric: LyricEntity) {
    db.lyricDao.update(lyric)
  }

  override suspend fun count(): Int {
    return db.lyricDao.count()
  }

  override suspend fun clearFavorite() {
    db.lyricDao.clearFavorite()
  }

  override suspend fun reset() {
    db.lyricDao.reset()
  }

  override fun observeLyrics(query: String): Flow<PagingData<LyricEntity>> {
    return Pager(
      config = PagingConfig(
        pageSize = SIZE,
        enablePlaceholders = false,
        maxSize = SIZE * 3
      ),
      pagingSourceFactory = { db.lyricDao.observeLyrics(query, version) }
    ).flow
  }

  override fun observeCategories(): Flow<List<Discover>> {
    return db.lyricDao.observeCategories(version)
  }

  override fun observeRecentLyrics(): Flow<List<LyricEntity>> {
    return db.lyricDao.observeRecentLyrics(version)
  }

  override fun getLyricsById(number: Int): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsById(number, version)
  }

  override fun getLyricsByCategory(id: Int): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsByCategory(id, version)
  }

  override fun findLyricById(id: Int): Flow<LyricEntity> {
    return db.lyricDao.findLyricById(id, version)
  }

  override val queryRandom = db.lyricDao.queryRandom(version)
  override val mostVisited = db.lyricDao.mostVisited(version)
  override val favorite = db.lyricDao.observeFavoriteLyrics(version)
}
