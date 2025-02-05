package net.techandgraphics.hymn.data

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.OtherEntity
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other

fun Lyric.asEntity() = LyricEntity(
  lyricId = lyricId,
  categoryId = categoryId,
  categoryName = categoryName,
  number = number,
  chorus = chorus,
  content = content,
  timestamp = timestamp,
  favorite = favorite,
  title = title,
  lang = lang,
)

fun Other.asEntity() = OtherEntity(
  groupName = groupName,
  content = content,
  lang = lang,
  resourceId = resourceId
)
