package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import javax.inject.Inject

class TimestampRepositoryImpl @Inject constructor(database: Database) : TimestampRepository {

  private val dao = database.timestampDao

  override suspend fun query(): List<TimestampEntity> {
    return dao.query()
  }

  override suspend fun upsert(items: List<TimestampEntity>) {
    dao.upsert(items)
  }

  override suspend fun delete(item: TimestampEntity) {
    dao.delete(item)
  }
}
