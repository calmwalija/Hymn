package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.ui.screen.settings.export.TimestampExport
import javax.inject.Inject

class TimestampRepositoryImpl @Inject constructor(database: Database) : TimestampRepository {

  private val dao = database.timestampDao

  override suspend fun toExport(): List<TimestampExport> {
    return dao.toExport().map { TimestampExport(it.number, it.lang, it.timestamp) }
  }

  override suspend fun upsert(items: List<TimestampEntity>) {
    dao.upsert(items)
  }

  override suspend fun delete(item: TimestampEntity) {
    dao.delete(item)
  }

  override suspend fun import(timestamp: TimestampExport) {
    if (ifExist(timestamp.toEntity()) == 0) {
      upsert(listOf(timestamp.toEntity()))
    }
  }

  override suspend fun ifExist(timestampEntity: TimestampEntity): Int {
    return dao.ifExist(timestampEntity.lang, timestampEntity.number, timestampEntity.timestamp)
  }
}
