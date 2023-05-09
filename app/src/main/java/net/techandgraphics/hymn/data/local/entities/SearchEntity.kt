package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity

@Entity(primaryKeys = ["query"], tableName = "Search")
data class SearchEntity(
  val id: Long = System.currentTimeMillis(),
  val query: String,
  val tag: String,
  val lang: String = "en",
)
