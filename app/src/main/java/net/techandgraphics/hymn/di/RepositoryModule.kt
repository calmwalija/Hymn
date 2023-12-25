package net.techandgraphics.hymn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.repository.CategoryRepositoryImpl
import net.techandgraphics.hymn.data.repository.LyricRepositoryImpl
import net.techandgraphics.hymn.data.repository.OtherRepositoryImpl
import net.techandgraphics.hymn.data.repository.SearchRepositoryImpl
import net.techandgraphics.hymn.data.repository.TimestampRepositoryImpl
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  abstract fun providesLyricRepository(p0: LyricRepositoryImpl): LyricRepository

  @Binds
  abstract fun providesCategoryRepository(p0: CategoryRepositoryImpl): CategoryRepository

  @Binds
  abstract fun providesOtherRepository(p0: OtherRepositoryImpl): OtherRepository

  @Binds
  abstract fun providesSearchRepository(p0: SearchRepositoryImpl): SearchRepository

  @Binds
  abstract fun providesTimestampRepository(p0: TimestampRepositoryImpl): TimestampRepository
}
