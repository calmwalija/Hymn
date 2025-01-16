package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.BaseRepository

interface TimestampRepository : BaseRepository<TimestampEntity> {

  suspend fun query(): List<TimestampEntity>
}
