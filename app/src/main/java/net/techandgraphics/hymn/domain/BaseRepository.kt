package net.techandgraphics.hymn.domain

interface BaseRepository<T> {

  suspend fun upsert(items: List<T>)

  suspend fun delete(item: T)
}
