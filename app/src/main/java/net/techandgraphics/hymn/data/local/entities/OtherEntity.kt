package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Translation

@Entity(tableName = "Other")
data class OtherEntity(
  val groupName: String,
  val content: String,
  val lang: String = Translation.EN.lowercase(),
  @PrimaryKey(autoGenerate = false) val resourceId: Int,
)
