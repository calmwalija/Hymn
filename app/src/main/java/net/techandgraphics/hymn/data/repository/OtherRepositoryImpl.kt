package net.techandgraphics.hymn.data.repository

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.Other
import net.techandgraphics.hymn.domain.repository.OtherRepository

@Singleton
class OtherRepositoryImpl @Inject constructor(
  db: Database,
  private val version: String
) : OtherRepository {

  private val dao = db.otherDao

  override suspend fun insert(resource: List<Other>) {
    dao.insert(resource)
  }

  override suspend fun clear() {
    dao.clear()
  }

  override fun observeOther(): Flow<List<Other>> {
    return dao.observeOther(version)
  }
}