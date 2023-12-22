package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Lang

@Entity(tableName = "Other")
data class EssentialEntity(
  val groupName: String,
  val content: String,
  val lang: String = Lang.EN.lowercase(),
  @PrimaryKey(autoGenerate = false) val resourceId: Int,
)
