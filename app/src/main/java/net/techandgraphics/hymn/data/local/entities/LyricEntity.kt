package net.techandgraphics.hymn.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "lyric")
@Parcelize
data class LyricEntity(
  @PrimaryKey(autoGenerate = false)
  val lyricId: Int,
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
  val lang: String = "en",
) : Parcelable
