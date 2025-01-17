package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import javax.inject.Inject

class TimeSpentRepositoryImpl @Inject constructor(database: Database) : TimeSpentRepository {

  private val dao = database.timeSpentDao

  override suspend fun query(): List<TimeSpentEntity> {
    return dao.query()
  }

  override suspend fun upsert(items: List<TimeSpentEntity>) {
    dao.upsert(items)
  }

  override suspend fun delete(item: TimeSpentEntity) {
    dao.delete(item)
  }
}
