package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.ui.screen.settings.export.SearchExport

interface SearchRepository {
  suspend fun upsert(data: List<SearchEntity>)
  suspend fun delete(data: SearchEntity)
  suspend fun clear()
  fun query(): Flow<List<SearchEntity>>
  suspend fun toExport(): List<SearchExport>
}
