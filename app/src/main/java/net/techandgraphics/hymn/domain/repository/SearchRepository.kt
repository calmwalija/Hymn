package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.SearchEntity

interface SearchRepository {
  suspend fun insert(searchEntities: List<SearchEntity>)
  suspend fun delete(searchEntity: SearchEntity)
  suspend fun clear()

  val query: Flow<List<SearchEntity>>
}
