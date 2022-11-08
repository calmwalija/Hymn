package net.techandgraphics.hymn.domain.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize


@Parcelize
data class Discover(
  @Embedded
  val lyric: Lyric,
  val count: Int
) : Parcelable
