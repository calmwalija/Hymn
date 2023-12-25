package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Category

interface CategoryRepository {
  suspend fun spotlighted(): List<Category>
  fun query(): Flow<List<Category>>
  fun queryById(id: Int): Flow<List<Category>>
}
