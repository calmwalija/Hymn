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
import net.techandgraphics.hymn.data.repository.*
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
        version: String
    ): Repository {
        return Repository(
            OtherRepositoryImpl(db),
            LyricRepositoryImpl(db, version),
            SearchRepositoryImpl(db, version),
            JsonParserImpl(db, app)
        )
    }
}