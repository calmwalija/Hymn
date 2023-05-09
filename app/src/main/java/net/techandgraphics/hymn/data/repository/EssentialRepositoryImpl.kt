package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.EssentialEntity
import net.techandgraphics.hymn.domain.repository.EssentialRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EssentialRepositoryImpl @Inject constructor(
  db: Database,
  private val version: String
) : EssentialRepository {

  private val dao = db.essentialDao

  override suspend fun insert(item: List<EssentialEntity>) {
    dao.insert(item)
  }

  override suspend fun clear() {
    dao.clear()
  }

  override val query = dao.query(version)
}
