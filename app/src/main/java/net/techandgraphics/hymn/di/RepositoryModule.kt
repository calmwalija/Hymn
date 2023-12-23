package net.techandgraphics.hymn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.ComplementaryParser
import net.techandgraphics.hymn.data.JsonParser
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.repository.LyricRepositoryImpl
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.data.repository.SearchRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Provides
  fun providesRepository(
    db: Database,
    version: String,
    @ApplicationContext context: Context
  ): Repository {
    return Repository(
      LyricRepositoryImpl(db, version, context),
      SearchRepositoryImpl(db, version),
    )
  }

  @Provides
  fun providesJsonParser(
    db: Database,
    @ApplicationContext context: Context
  ) = JsonParser(db, context)

  @Provides
  fun providesComplementaryParser(
    db: Database,
    @ApplicationContext context: Context
  ) = ComplementaryParser(db, context)
}
