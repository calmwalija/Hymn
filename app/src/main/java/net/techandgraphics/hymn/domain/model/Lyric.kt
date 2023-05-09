package net.techandgraphics.hymn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lyric(
  val lyricId: Int,
  val categoryId: Int,
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val timestamp: Long = System.currentTimeMillis(),
  val favorite: Boolean = false,
  val topPick: String,
  val topPickHit: Int,
  val title: String,
  val lang: String
) : Parcelable
