package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.Other

interface OtherRepository {
  suspend fun insert(resource: List<Other>)
  suspend fun clear()
  fun observeOther(): Flow<List<Other>>
}