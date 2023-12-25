package net.techandgraphics.hymn.domain

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

fun LyricEntity.asModel() = Lyric(
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

fun OtherEntity.asModel() = Other(
  groupName = groupName,
  content = content,
  lang = lang,
  resourceId = resourceId
)

fun SearchEntity.asModel() = Search(
  query = query,
  tag = tag,
  lang = lang,
  id = id
)

fun TimestampEntity.asModel() = Timestamp(
  number = number,
  lang = lang,
  timestamp = timestamp,
  id = id
)

fun CategoryEmbedded.asModel() = Category(
  lyric = lyric.asModel(),
  count = count
)

fun Lyric.toTimestampEntity() = TimestampEntity(
  number = number,
  lang = lang
)
