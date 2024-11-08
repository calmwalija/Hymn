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
import net.techandgraphics.hymn.uniquelyCraftedKey
import net.techandgraphics.hymn.uniquelyCraftedKeyToList
import javax.inject.Inject
import kotlin.random.Random

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
      dao.diveInto(getLang()).map { it.map { data -> data.asModel() } }
    }
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

  override fun uniquelyCrafted(count: Int): Flow<List<Lyric>> {
    return runBlocking {
      val keys = (prefs uniquelyCraftedKey count).uniquelyCraftedKeyToList()
      val leftKey = keys.firstOrNull() ?: count.plus(1)
      val rightKey = keys.lastOrNull() ?: count.minus(1)
      val leftKey1 = Random.nextInt(rightKey)
      val leftKey2 = Random.nextInt(rightKey)
      val leftKey3 = Random.nextInt(rightKey)
      val leftKey4 = Random.nextInt(rightKey)
      dao.uniquelyCrafted(getLang(), leftKey, rightKey, leftKey1, leftKey2, leftKey3, leftKey4)
    }
  }

  override suspend fun favorite(favorite: Boolean, number: Int) {
    dao.favorite(favorite, number, getLang())
  }

  override suspend fun read(number: Int, timestamp: Long) {
    dao.read(number, timestamp, getLang())
  }

  override suspend fun backup(): List<Lyric> {
    return dao.backup().map { it.asModel() }
  }

  override suspend fun getLastHymn(): Int = dao.getLastHymn()
}
