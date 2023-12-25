package net.techandgraphics.hymn.data.local.join

import androidx.room.Embedded
import net.techandgraphics.hymn.data.local.entities.LyricEntity

data class CategoryEmbedded(
  @Embedded val lyric: LyricEntity,
  val count: String
)
