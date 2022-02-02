package net.techandgraphics.hymn.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import net.techandgraphics.hymn.db.Database
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.models.Other
import net.techandgraphics.hymn.models.Search
import net.techandgraphics.hymn.utils.Constant
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.regexLowerCase
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

class Repository @Inject constructor(
    private val db: Database,
    @ApplicationContext val context: Context
) {


    suspend fun jsonLyricToDB(): Boolean {
        if (db.lyricDao.observeLyrics().first().isEmpty()) {

            var ofType = object : TypeToken<List<Lyric>>() {}.type
            (Gson().fromJson(
                Utils.readJsonFromAssetToString(context, "lyrics.json")!!, ofType
            ) as List<Lyric>).also { lyric ->

                val data = lyric.map {
                    val string: List<String> = it.content.split(" ")
                    val data = buildString {
                        for (i in 0..2) {
                            append(string[i])
                        }
                    }

                    val title = it.content.substring(0, it.content.indexOf("\n")).regexLowerCase()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                    it.copy(topPick = data.regexLowerCase().replace(" ", ""), title = title)
                }


                db.lyricDao.insert(data)
                db.searchDao.insert(Constant.searchTag)

            }


            ofType = object : TypeToken<List<Other>>() {}.type
            (Gson().fromJson(
                Utils.readJsonFromAssetToString(context, "other.json")!!, ofType
            ) as List<Other>).also { db.otherDao.insert(it) }

        }

        return false
    }


    fun observeHymns(query: String) = db.lyricDao.observeLyrics(query)
    val observeCategories = db.lyricDao.observeCategories()
    val observeTopPickCategories = db.lyricDao.observeTopPickCategories()
    val observeRecentLyrics = db.lyricDao.observeRecentLyrics()
    val observeOther = db.otherDao.observeOther()
    val observeFavoriteLyrics = db.lyricDao.observeFavoriteLyrics()

    fun getLyricsById(lyric: Lyric) = db.lyricDao.getLyricsById(lyric.number)
    fun findLyricById(id: Int) = db.lyricDao.findLyricById(id)
    fun observeSortBy(sortBy:String) = db.lyricDao.observeSortBy(sortBy)!!
    suspend fun clearFavorite() = db.lyricDao.clearFavorite()


    suspend fun update(lyric: Lyric) = db.lyricDao.update(lyric)
    fun getLyricsByCategory(lyric: Lyric) = db.lyricDao.getLyricsByCategory(lyric.categoryId)

    suspend fun insert(list: List<Search>) = db.searchDao.insert(list)
    suspend fun delete(list: Search) = db.searchDao.delete(list)
    val observeSearch = db.searchDao.observeSearch()

}