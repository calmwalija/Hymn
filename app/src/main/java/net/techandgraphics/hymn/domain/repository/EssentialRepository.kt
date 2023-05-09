package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.entities.EssentialEntity

interface EssentialRepository {
  suspend fun insert(item: List<EssentialEntity>)
  suspend fun clear()
  val query: Flow<List<EssentialEntity>>
}
