package net.techandgraphics.hymn.data.repository


data class Repository(
    val otherRepositoryImpl: OtherRepositoryImpl,
    val lyricRepositoryImpl: LyricRepositoryImpl,
    val searchRepositoryImpl: SearchRepositoryImpl,
    val jsonParserImpl: JsonParserImpl
)
