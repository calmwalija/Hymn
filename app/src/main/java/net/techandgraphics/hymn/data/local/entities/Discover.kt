package net.techandgraphics.hymn.data.local.entities

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize
import net.techandgraphics.hymn.domain.model.Lyric

@Parcelize
data class Discover(
  @Embedded
  val lyric: Lyric,
  val count: Int
) : Parcelable
