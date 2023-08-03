package net.techandgraphics.hymn

import net.techandgraphics.hymn.data.local.entities.EssentialEntity
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.domain.model.Essential
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search

fun LyricEntity.asLyric() = Lyric(
  lyricId,
  categoryId,
  categoryName,
  number,
  chorus,
  content,
  timestamp,
  favorite,
  topPick,
  topPickHit,
  title,
  lang,
  isOnPlaylist,
  justAdded,
  millsAdded
)

fun Lyric.asLyricEntity() = LyricEntity(
  lyricId,
  categoryId,
  categoryName,
  number,
  chorus,
  content,
  timestamp,
  favorite,
  topPick,
  topPickHit,
  title,
  lang,
  isOnPlaylist,
  justAdded,
  millsAdded
)

fun EssentialEntity.asEssential() = Essential(resourceId, groupName, content, lang)
fun SearchEntity.asSearch() = Search(id, query, tag, lang)
fun Search.asSearchEntity() = SearchEntity(id, query, tag, lang)
