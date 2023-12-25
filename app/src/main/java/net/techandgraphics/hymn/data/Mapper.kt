package net.techandgraphics.hymn.data

import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.OtherEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.data.local.join.CategoryEmbedded
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.domain.model.Timestamp

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

fun Search.asEntity() = SearchEntity(
  query = query,
  tag = tag,
  lang = lang,
  id = id
)

fun Timestamp.asEntity() = TimestampEntity(
  number = number,
  lang = lang,
  timestamp = timestamp,
  id = id
)

fun Category.asEntity() = CategoryEmbedded(
  lyric = lyric.asEntity(),
  count = count
)
