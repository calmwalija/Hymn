package net.techandgraphics.hymn.domain.repository

import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.domain.model.Other

interface OtherRepository {
    suspend fun insert(resource: List<Other>)
    suspend fun clear()
    fun observeOther(): Flow<List<Other>>
}