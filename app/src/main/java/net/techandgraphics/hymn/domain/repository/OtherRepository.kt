package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.domain.model.Other

interface OtherRepository {
  suspend fun upsert(data: List<Other>)
  suspend fun query(): List<Other>
  suspend fun clearAll()
}
