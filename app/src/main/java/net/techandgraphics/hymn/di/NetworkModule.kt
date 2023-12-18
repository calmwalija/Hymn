package net.techandgraphics.hymn.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.remote.RemoteSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .addInterceptor {
        HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BASIC
        }
        val request = it.request()
          .newBuilder()
          .addHeader("Accept", "application/json")
          .build()
        it.proceed(request)
      }
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .build()
  }

  @Provides
  fun providesRetrofit(
    okHttpClient: OkHttpClient,
  ): Retrofit {
    return Retrofit.Builder()
      .baseUrl(RemoteSource.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
  }

  @Singleton
  @Provides
  fun providesRemoteSource(
    okHttpClient: OkHttpClient,
  ): RemoteSource =
    Retrofit.Builder()
      .baseUrl(RemoteSource.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttpClient)
      .build()
      .create(RemoteSource::class.java)
}
