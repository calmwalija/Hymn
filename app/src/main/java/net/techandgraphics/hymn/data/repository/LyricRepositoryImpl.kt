package net.techandgraphics.hymn.data.repository

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricRepositoryImpl @Inject constructor(
    private val db: Database,
    val context: Context,
    private val version: String
) : LyricRepository {

    override suspend fun insert(lyric: List<Lyric>) {
        db.lyricDao.insert(lyric)
    }

    override suspend fun update(lyric: Lyric) {
        db.lyricDao.update(lyric)
    }

    override suspend fun count(): Int {
        return db.lyricDao.count()
    }

    override suspend fun clearFavorite() {
        db.lyricDao.clearFavorite()
    }

    override suspend fun clear() {
        db.lyricDao.clear()
    }

    override fun observeLyrics(query: String): Flow<PagingData<Lyric>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                maxSize = 20 * 3
            ),
            pagingSourceFactory = { db.lyricDao.observeLyrics(query, version) }
        ).flow
    }

    override fun observeCategories(): Flow<List<Lyric>> {
        return db.lyricDao.observeCategories(version)
    }

    override fun observeTopPickCategories(): Flow<List<Lyric>> {
        return db.lyricDao.observeTopPickCategories(version)
    }

    override fun observeRecentLyrics(): Flow<List<Lyric>> {
        return db.lyricDao.observeRecentLyrics(version)
    }

    override fun getLyricsById(number: Int): Flow<List<Lyric>> {
        return db.lyricDao.getLyricsById(number, version)
    }

    override fun getLyricsByCategory(id: Int): Flow<List<Lyric>> {
        return db.lyricDao.getLyricsByCategory(id, version)
    }

    override fun observeFavoriteLyrics(): Flow<List<Lyric>> {
        return db.lyricDao.observeFavoriteLyrics()
    }

    override fun findLyricById(id: Int): Flow<Lyric> {
        return db.lyricDao.findLyricById(id, version)
    }


}