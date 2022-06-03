package net.techandgraphics.hymn.data.repository

import net.techandgraphics.hymn.domain.repository.JsonParser
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository


data class Repository(
    val otherRepository: OtherRepository,
    val lyricRepository: LyricRepository,
    val searchRepository: SearchRepository,
    val jsonParser: JsonParser
)
