package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.domain.repository.EssentialRepository
import net.techandgraphics.hymn.domain.repository.JsonParser
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository

data class Repository(
  val essentialRepository: EssentialRepository,
  val lyricRepository: LyricRepository,
  val searchRepository: SearchRepository,
  val jsonParser: JsonParser
)
