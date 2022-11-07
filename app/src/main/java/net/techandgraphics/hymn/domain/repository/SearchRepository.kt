package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Search

interface SearchRepository {
  suspend fun insert(search: List<Search>)
  suspend fun delete(search: Search)
  suspend fun clear()
  fun observeSearch(): Flow<List<Search>>
}