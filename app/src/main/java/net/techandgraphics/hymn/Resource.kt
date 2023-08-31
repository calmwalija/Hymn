package net.techandgraphics.hymn

sealed class Resource<T>(
  val data: T? = null,
  val exception: Exception? = null
) {
  class Success<T>(data: T? = null) : Resource<T>(data)
  class Loading<T>(data: T? = null) : Resource<T>(data)
  class Error<T>(data: T? = null, exception: Exception? = null) : Resource<T>(data, exception)
}
