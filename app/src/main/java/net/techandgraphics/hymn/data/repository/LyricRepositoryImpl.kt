package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.prefs.SharedPrefs
import net.techandgraphics.hymn.data.prefs.getLang
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

class LyricRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: SharedPrefs
) : LyricRepository {

  private val dao = database.lyricDao
  private val pageSize = 20
  override fun query(query: String): Flow<List<Lyric>> {
    return dao.query(query, prefs.getLang()).map { it.map { it.asModel() } }
  }

  override fun queryByCategory(id: Int): Flow<List<Lyric>> {
    return dao.queryByCategory(id, prefs.getLang()).map { it.map { it.asModel() } }
  }

  override fun diveInto(): Flow<List<Lyric>> {
    return dao.diveInto(prefs.getLang()).map { it.map { it.asModel() } }
  }

  override fun queryById(lyricId: Int): Flow<List<Lyric>> {
    return dao.queryById(lyricId).map { it.map { it.asModel() } }
  }

  override fun favorites(): Flow<List<Lyric>> {
    return dao.favorites(prefs.getLang()).map { it.map { it.asModel() } }
  }

  override suspend fun upsert(lyric: List<Lyric>) {
    dao.upsert(lyric.map { it.asEntity() })
  }

  override suspend fun queryByNumber(number: Int): List<Lyric> {
    return dao.queryByNumber(number).map { it.asModel() }
  }

  override fun uniquelyCrafted(): Flow<List<Lyric>> {
    return dao.uniquelyCrafted(prefs.getLang())
  }

  override suspend fun favorite(favorite: Boolean, number: Int) {
    dao.favorite(favorite, number, prefs.getLang())
  }

  override suspend fun read(number: Int, timestamp: Long) {
    dao.read(number, timestamp, prefs.getLang())
  }

  override suspend fun backup(): List<Lyric> {
    return dao.backup().map { it.asModel() }
  }
}
