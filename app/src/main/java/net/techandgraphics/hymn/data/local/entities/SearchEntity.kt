package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import net.techandgraphics.hymn.data.local.Lang

@Entity(primaryKeys = ["query"], tableName = "Search")
data class SearchEntity(
  val query: String,
  val tag: String,
  val lang: String = Lang.EN.name,
  val id: Long = System.currentTimeMillis(),
)
