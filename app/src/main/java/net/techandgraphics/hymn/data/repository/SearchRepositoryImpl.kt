package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.domain.repository.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepositoryImpl @Inject constructor(
  db: Database,
  val version: String,
) : SearchRepository {

  private val dao = db.searchDao

  override suspend fun insert(searchEntities: List<SearchEntity>) {
    dao.insert(searchEntities.map { it.copy(lang = version) })
  }

  override suspend fun delete(searchEntity: SearchEntity) {
    dao.delete(searchEntity)
  }

  override suspend fun clear() {
    dao.clear()
  }

  override val query = dao.query(version)
}
