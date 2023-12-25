package net.techandgraphics.hymn.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.prefs.Prefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricRepositoryImpl @Inject constructor(database: Database, prefs: Prefs) : LyricRepository {

  private val dao = database.lyricDao
  private val lang = prefs.lang
  private val pageSize = 20

  override fun query(query: String): Flow<PagingData<Lyric>> {
    return Pager(
      config = PagingConfig(
        pageSize = pageSize,
        maxSize = pageSize.times(3)
      ),
      pagingSourceFactory = { dao.query(query, lang) }
    )
      .flow
      .map { it.map { it.asModel() } }
  }

  override fun queryByCategory(id: Int): Flow<List<Lyric>> {
    return dao.queryByCategory(id, lang).map { it.map { it.asModel() } }
  }

  override fun theHymn(): Flow<List<Lyric>> {
    return dao.theHymn(lang).map { it.map { it.asModel() } }
  }

  override fun queryById(lyricId: Int): Flow<List<Lyric>> {
    return dao.queryById(lyricId).map { it.map { it.asModel() } }
  }

  override fun favorites(): Flow<List<Lyric>> {
    return dao.favorites(lang).map { it.map { it.asModel() } }
  }

  override suspend fun upsert(lyric: List<Lyric>) {
    dao.upsert(lyric.map { it.asEntity() })
  }

  override suspend fun queryByNumber(number: Int): List<Lyric> {
    return dao.queryByNumber(number).map { it.asModel() }
  }

  override suspend fun queryId(): Int? {
    return dao.queryId(lang)
  }

  override suspend fun favorite(favorite: Boolean, number: Int) {
    dao.favorite(favorite, number, lang)
  }

  override suspend fun read(number: Int, timestamp: Long) {
    dao.read(number, timestamp, lang)
  }

  override suspend fun backup(): List<Lyric> {
    return dao.backup().map { it.asModel() }
  }
}
