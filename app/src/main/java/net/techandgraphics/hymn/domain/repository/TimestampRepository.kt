package net.techandgraphics.hymn.domain.repository

import net.techandgraphics.hymn.domain.model.Timestamp

interface TimestampRepository {
  suspend fun upsert(data: Timestamp)
}
