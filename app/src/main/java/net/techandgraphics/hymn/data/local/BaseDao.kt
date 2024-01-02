package net.techandgraphics.hymn.data.local

import androidx.room.Delete
import androidx.room.Upsert

interface BaseDao<T> {

  @Upsert
  suspend fun upsert(items: List<T>)

  @Delete
  suspend fun delete(item: T)
}
