package net.techandgraphics.hymn.domain.repository


interface JsonParser {
    suspend fun fromJson(): Boolean
}