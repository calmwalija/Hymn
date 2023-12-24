package net.techandgraphics.hymn

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

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

fun SearchEntity.asModel() = Search(
  id = id,
  query = query,
  tag = tag,
  lang = lang
)

fun Search.asEntity() = SearchEntity(
  query = query,
  tag = tag,
  lang = lang,
  id = id
)

fun LyricEntity.asTimestamp() = TimestampEntity(
  number = number,
  lang = lang
)
