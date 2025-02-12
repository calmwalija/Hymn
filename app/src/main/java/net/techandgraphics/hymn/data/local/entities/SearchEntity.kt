package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import net.techandgraphics.hymn.data.local.Translation

@Entity(primaryKeys = ["query"], tableName = "Search")
data class SearchEntity(
  val query: String,
  val tag: String,
  val lang: String = Translation.EN.lowercase(),
  val id: Long = System.currentTimeMillis(),
)
