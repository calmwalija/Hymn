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
  val favorite: Boolean = false,
  val title: String = "",
  val lang: String = Lang.EN.name.lowercase(),
  val timestamp: Long = System.currentTimeMillis(),

//  val topPickHit: Int = 0,
//  val topPick: String = "0",
//  val forTheService: Boolean = false,
//  val ftsSuggestion: Boolean = false,
//  val justAdded: Boolean = false,
//  val millsAdded: Long = System.currentTimeMillis(),
)
