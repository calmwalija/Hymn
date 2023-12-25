package net.techandgraphics.hymn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.parser.ComplementaryParser
import net.techandgraphics.hymn.data.parser.JsonParser

@Module
@InstallIn(SingletonComponent::class)
object ParserModule {

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
