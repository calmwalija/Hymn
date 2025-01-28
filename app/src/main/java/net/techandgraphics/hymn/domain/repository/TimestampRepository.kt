package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.BaseRepository
import net.techandgraphics.hymn.ui.screen.settings.export.TimestampExport

interface TimestampRepository : BaseRepository<TimestampEntity> {
  suspend fun toExport(): List<TimestampExport>
  suspend fun import(timestamp: TimestampExport)
  suspend fun ifExist(timestampEntity: TimestampEntity): Int
}
