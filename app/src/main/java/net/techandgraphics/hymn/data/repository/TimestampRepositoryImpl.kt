package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.domain.model.Timestamp
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import javax.inject.Inject

class TimestampRepositoryImpl @Inject constructor(database: Database) : TimestampRepository {

  private val dao = database.timestampDao

  override suspend fun upsert(data: Timestamp) {
    dao.upsert(data.asEntity())
  }
}
