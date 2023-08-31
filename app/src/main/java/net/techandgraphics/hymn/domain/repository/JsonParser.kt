package net.techandgraphics.hymn.domain.repository

interface JsonParser {
  suspend fun fromJson(event: suspend () -> Unit): Boolean
  suspend fun fromJsonToOther()
  suspend fun fromJsonToLyric(event: suspend () -> Unit)
}
