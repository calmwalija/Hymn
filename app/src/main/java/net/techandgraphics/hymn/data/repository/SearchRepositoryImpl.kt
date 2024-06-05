package net.techandgraphics.hymn.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
  database: Database,
  private val prefs: AppPrefs
) : SearchRepository {

  private val dao = database.searchDao

  override suspend fun upsert(data: List<SearchEntity>) {
    dao.upsert(data)
  }

  override suspend fun delete(data: SearchEntity) {
    dao.delete(data)
  }

  override suspend fun clear() {
    dao.clear()
  }

  override fun query(): Flow<List<SearchEntity>> {
    return runBlocking { dao.query(prefs.getLang()) }
  }
}
