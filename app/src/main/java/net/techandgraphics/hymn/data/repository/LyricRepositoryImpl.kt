package net.techandgraphics.hymn.data.repository

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.work.ListenableWorker
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.Resource
import net.techandgraphics.hymn.data.asLyricEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.remote.RemoteSource
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject
import javax.inject.Singleton

const val SIZE = 20

@Singleton
class LyricRepositoryImpl @Inject constructor(
  private val db: Database,
  private val version: String,
  private val api: RemoteSource,
  private val context: Context
) : LyricRepository {

  private fun LyricEntity.toJustAdded() = copy(justAdded = true)
  override suspend fun fetch(status: (Resource<List<LyricEntity>>) -> Unit): ListenableWorker.Result {
    val lastInsertedId = db.lyricDao.lastInsertedId() ?: 0
    return try {
      val lyrics = api.fetchLyric(lastInsertedId).map { it.asLyricEntity().toJustAdded() }
      JsonParserImpl(db, context).fromJsonToLyricImpl(lyrics, runSearchTag = false)
      db.lyricDao.getLyricsByIdRange(lastInsertedId).also {
        if (lyrics.isNotEmpty())
          status.invoke(Resource.Success(it))
      }
      ListenableWorker.Result.success()
    } catch (e: Exception) {
      status.invoke(Resource.Error(exception = e))
      Log.e("TAG", e.toString())
      ListenableWorker.Result.retry()
    }
  }

  override fun getLyricsByIdRangeLang(number: Long): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsByIdRangeLang(number, version)
  }

  override suspend fun count(): Int? {
    return db.lyricDao.count(version)
  }

  override suspend fun categoryCount(): List<Int> {
    return db.lyricDao.categoryCount(version)
  }

  override suspend fun insert(lyric: List<LyricEntity>) {
    db.lyricDao.insert(lyric)
  }

  override suspend fun update(lyric: LyricEntity) {
    db.lyricDao.update(lyric)
  }

  override suspend fun lastInsertedHymn(): Int? {
    return db.lyricDao.lastInsertedHymn(version)
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

  override fun featuredHymn(limit: Int): Flow<List<Discover>> {
    return db.lyricDao.featuredHymn(version, limit)
  }

  override fun getLyricsById(number: Int): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsById(number, version)
  }

  override fun getInverseLyricsById(version: String, id: Int): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsById(id, version)
  }

  override fun getLyricsByCategory(id: Int): Flow<List<LyricEntity>> {
    return db.lyricDao.getLyricsByCategory(id, version)
  }

  override fun findLyricById(id: Int): Flow<LyricEntity> {
    return db.lyricDao.findLyricById(id, version)
  }

  override val favorite = db.lyricDao.observeFavoriteLyrics(version)
  override val theHymn = db.lyricDao.theHymn(version)
  override val justAdded = db.lyricDao.justAdded(version)
}
