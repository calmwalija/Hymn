package net.techandgraphics.hymn.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.techandgraphics.hymn.data.repository.LyricRepositoryImpl
import net.techandgraphics.hymn.data.repository.OtherRepositoryImpl
import net.techandgraphics.hymn.data.repository.SearchRepositoryImpl
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun lyricRepository(
        lyricRepositoryImpl: LyricRepositoryImpl
    ): LyricRepository


    @Binds
    abstract fun otherRepository(
        otherRepositoryImpl: OtherRepositoryImpl
    ): OtherRepository


    @Binds
    abstract fun searchRepository(
        searchRepositoryImpl: SearchRepositoryImpl
    ): SearchRepository


}