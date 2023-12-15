package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Lang

@Entity(tableName = "lyric")
data class LyricEntity(
  @PrimaryKey(autoGenerate = false) val lyricId: Int,
  val categoryId: Int,
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val timestamp: Long = System.currentTimeMillis(),
  val favorite: Boolean = false,
  val topPick: String = "0",
  val topPickHit: Int = 0,
  val title: String = "",
  val lang: String = Lang.EN.name.lowercase(),
  val forTheService: Boolean = false,
  val ftsSuggestion: Boolean = false,
  val justAdded: Boolean = false,
  val millsAdded: Long = System.currentTimeMillis(),
)
