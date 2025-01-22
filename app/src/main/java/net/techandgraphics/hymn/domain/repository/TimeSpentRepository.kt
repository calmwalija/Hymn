package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.domain.BaseRepository
import net.techandgraphics.hymn.ui.screen.settings.export.TimeSpentExport

interface TimeSpentRepository : BaseRepository<TimeSpentEntity> {
  suspend fun toExport(): List<TimeSpentExport>
}
