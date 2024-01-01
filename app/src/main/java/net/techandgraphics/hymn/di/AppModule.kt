package net.techandgraphics.hymn.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Migration
import net.techandgraphics.hymn.data.local.Migration.MIGRATION_3_4
import net.techandgraphics.hymn.data.local.Migration.MIGRATION_4_5
import net.techandgraphics.hymn.data.prefs.SharedPrefs
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
  fun providesSharedPreferences(@ApplicationContext context: Context): SharedPrefs = SharedPrefs(context)
}
