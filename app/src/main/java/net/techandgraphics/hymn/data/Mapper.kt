package net.techandgraphics.hymn.data

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.remote.dto.LyricDto

fun LyricDto.asLyricEntity() = LyricEntity(
  lyricId,
  categoryId,
  categoryName,
  number,
  chorus,
  content,
  lang = lang,
  timestamp = 0L,
  justAdded = true
)
