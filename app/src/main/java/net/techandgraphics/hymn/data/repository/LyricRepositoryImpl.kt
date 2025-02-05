package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

class LyricRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: DataStorePrefs,
) : LyricRepository {

  private val dao = database.lyricDao
  private suspend fun getLang() = prefs.get(prefs.translationKey, Lang.EN.lowercase())

  override fun query(query: String): Flow<List<Lyric>> {
    return runBlocking {
      dao.query(query, getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun queryByCategory(id: Int): Flow<List<Lyric>> {
    return runBlocking {
      dao.queryByCategory(id, getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override fun diveInto(): Flow<List<Lyric>> {
    return runBlocking {
      dao.diveInto(getLang()).map { data -> data.map { it.asModel() } }
    }
  }

  override suspend fun toExport(): List<Int> {
    return dao.toExport()
  }

  override fun queryById(lyricId: Int): Flow<List<Lyric>> {
    return dao.queryById(lyricId).map { it.map { data -> data.asModel() } }
  }

  override fun favorites(): Flow<List<Lyric>> {
    return runBlocking {
      dao.favorites(getLang()).map { it.map { data -> data.asModel() } }
    }
  }

  override suspend fun upsert(lyric: List<Lyric>) {
    dao.upsert(lyric.map { it.asEntity() })
  }

  override suspend fun queryByNumber(number: Int): List<Lyric> {
    return dao.queryByNumber(number).map { it.asModel() }
  }

  override suspend fun uniquelyCrafted(): List<Lyric> {
    return runBlocking { dao.uniquelyCrafted(getLang()) }
  }

  override suspend fun favorite(favorite: Boolean, number: Int) {
    dao.favorite(favorite, number, getLang())
  }

  override suspend fun read(number: Int, timestamp: Long, lang: String?) {
    dao.read(number, timestamp, lang ?: getLang())
  }

  override suspend fun getLastHymn(lang: String): Int = dao.getLastHymn(lang)

  override suspend fun emptyStateSuggested() =
    dao.emptyStateSuggested(getLang()).map { it.asModel() }
}
