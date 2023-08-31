package net.techandgraphics.hymn.data.remote

import net.techandgraphics.hymn.data.remote.dto.LyricDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteSource {

  companion object {
    const val BASE_URL = "https://hymn.techandgraphics.net/api/v1/"
//    const val BASE_URL = "http://192.168.8.166:8000/api/v1/"
  }

  @GET("lyric/{lastInsertedId}/")
  suspend fun fetchLyric(
    @Path("lastInsertedId")
    id: Long
  ): List<LyricDto>
}
