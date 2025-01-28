package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.ui.screen.settings.export.TimeSpentExport
import javax.inject.Inject

class TimeSpentRepositoryImpl @Inject constructor(database: Database) : TimeSpentRepository {

  private val dao = database.timeSpentDao

  override suspend fun toExport(): List<TimeSpentExport> {
    return dao.toExport().map { TimeSpentExport(it.number, it.lang, it.timeSpent) }
  }

  override suspend fun getCount(timeSpentEntity: TimeSpentEntity): Int {
    return dao.getCount(timeSpentEntity.number, timeSpentEntity.lang, timeSpentEntity.timeSpent)
  }

  override suspend fun upsert(items: List<TimeSpentEntity>) {
    dao.upsert(items)
  }

  override suspend fun delete(item: TimeSpentEntity) {
    dao.delete(item)
  }
}
