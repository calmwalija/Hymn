package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

class LyricRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: AppPrefs
) : LyricRepository {

  private val dao = database.lyricDao

  override fun query(query: String): Flow<List<Lyric>> {
    return runBlocking {
      dao.query(query, prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun queryByCategory(id: Int): Flow<List<Lyric>> {
    return runBlocking {
      dao.queryByCategory(id, prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun diveInto(): Flow<List<Lyric>> {
    return runBlocking {
      dao.diveInto(prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun queryById(lyricId: Int): Flow<List<Lyric>> {
    return dao.queryById(lyricId).map { it.map { data -> data.asModel() } }
  }

  override fun favorites(): Flow<List<Lyric>> {
    return runBlocking {
      dao.favorites(prefs.getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override suspend fun upsert(lyric: List<Lyric>) {
    dao.upsert(lyric.map { it.asEntity() })
  }

  override suspend fun queryByNumber(number: Int): List<Lyric> {
    return dao.queryByNumber(number).map { it.asModel() }
  }

  override fun uniquelyCrafted(): Flow<List<Lyric>> {
    return runBlocking {
      dao.uniquelyCrafted(prefs.getLang())
    }
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
