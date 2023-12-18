package net.techandgraphics.hymn.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface BaseDao<T> {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun upsert(items: List<T>)

  @Delete
  suspend fun delete(item: T)
}
