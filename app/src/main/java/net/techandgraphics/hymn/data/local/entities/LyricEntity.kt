package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Translation

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
  val lang: String = Translation.EN.name.lowercase(),
  val timestamp: Long = System.currentTimeMillis(),
)
