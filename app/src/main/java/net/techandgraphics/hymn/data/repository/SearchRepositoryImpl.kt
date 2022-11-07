package net.techandgraphics.hymn.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.domain.repository.SearchRepository

@Singleton
class SearchRepositoryImpl @Inject constructor(
  db: Database,
  val version: String,
) : SearchRepository {

  private val dao = db.searchDao

  override suspend fun insert(search: List<Search>) {
    dao.insert(search.map { it.copy(lang = version) })
  }

  override suspend fun delete(search: Search) {
    dao.delete(search)
  }

  override suspend fun clear() {
    dao.clear()
  }

  override fun observeSearch(): Flow<List<Search>> {
    return dao.observeSearch(version)
  }
}