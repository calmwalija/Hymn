package net.techandgraphics.hymn.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.Migration
import net.techandgraphics.hymn.data.local.Migration.MIGRATION_3_4
import net.techandgraphics.hymn.data.local.Migration.MIGRATION_4_5
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @Singleton
  fun providesDatabase(
    @ApplicationContext context: Context
  ): Database = Room.databaseBuilder(context, Database::class.java, "hymn_repo")
    .addMigrations(Migration.MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
    .build()

  @Provides
  fun providesLang(app: Application): String =
    PreferenceManager.getDefaultSharedPreferences(app)
      .getString(app.getString(R.string.translation_key), Lang.EN.lowercase())!!

  @Provides
  @Singleton
  fun providesSharedPreferences(app: Application): SharedPreferences =
    PreferenceManager.getDefaultSharedPreferences(app)
}
