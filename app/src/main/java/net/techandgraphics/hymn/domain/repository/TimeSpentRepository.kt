package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.domain.BaseRepository

interface TimeSpentRepository : BaseRepository<TimeSpentEntity> {
  suspend fun query(): List<TimeSpentEntity>
}
