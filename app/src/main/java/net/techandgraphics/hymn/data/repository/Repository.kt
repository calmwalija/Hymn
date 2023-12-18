package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository

data class Repository(
  val lyricRepository: LyricRepository,
  val searchRepository: SearchRepository,
)
