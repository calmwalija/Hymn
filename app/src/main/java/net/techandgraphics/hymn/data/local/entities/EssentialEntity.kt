package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Other")
data class EssentialEntity(
  val groupName: String,
  val content: String,
  val lang: String = "en",
  @PrimaryKey(autoGenerate = false) val resourceId: Int,
)
