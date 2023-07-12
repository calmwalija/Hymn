package net.techandgraphics.hymn.di

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Migration
import net.techandgraphics.hymn.data.remote.RemoteSource
import net.techandgraphics.hymn.data.repository.EssentialRepositoryImpl
import net.techandgraphics.hymn.data.repository.JsonParserImpl
import net.techandgraphics.hymn.data.repository.LyricRepositoryImpl
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.data.repository.SearchRepositoryImpl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun providesDatabase(
    @ApplicationContext context: Context
  ): Database = Room.databaseBuilder(context, Database::class.java, "hymn_repo")
    .addMigrations(Migration.MIGRATION_2_3)
    .build()

  @Singleton
  @Provides
  fun providesFirebaseAnalytics(
    @ApplicationContext context: Context
  ): FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

  @Provides
  fun providesLang(app: Application): String =
    PreferenceManager.getDefaultSharedPreferences(app)
      .getString(app.getString(R.string.version_key), "en")!!

  @Provides
  fun providesRepository(
    db: Database,
    app: Application,
    version: String,
    remoteSource: RemoteSource,
    @ApplicationContext
    context: Context
  ): Repository {
    return Repository(
      EssentialRepositoryImpl(db, version),
      LyricRepositoryImpl(db, version, remoteSource, context),
      SearchRepositoryImpl(db, version),
      JsonParserImpl(db, app)
    )
  }

  @Provides
  fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .addInterceptor {
      val request = it.request()
        .newBuilder()
        .addHeader("Accept", "application/json")
        .build()
//      Log.e("TAG", "providesOkHttpClient: " + request.url())
      it.proceed(request)
    }
    .build()

  @Singleton
  @Provides
  fun providesRemoteSource(): RemoteSource =
    Retrofit.Builder()
      .baseUrl(RemoteSource.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .client(providesOkHttpClient())
      .build()
      .create(RemoteSource::class.java)
}
