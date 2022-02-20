package net.techandgraphics.hymn.data

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.db.Database
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.models.Other
import net.techandgraphics.hymn.models.Search
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.regexLowerCase
import java.util.*
import javax.inject.Inject

class Repository @Inject constructor(
    private val db: Database,
    @ApplicationContext val context: Context
) {

    private val version =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getString(context.getString(R.string.version_key), "en")!!

    suspend fun jsonLyricToDB(): Boolean {
        var ofType = object : TypeToken<List<Lyric>>() {}.type
        (Gson().fromJson(
            Utils.readJsonFromAssetToString(context, "lyrics.json")!!, ofType
        ) as List<Lyric>).also { lyric ->
            if (lyric.size != db.lyricDao.count()) {

                val data = lyric.map {
                    val string: List<String> = it.content.split(" ")
                    val data = buildString {
                        try {
                            for (i in 0..2) {
                                append(string[i])
                            }
                        } catch (e: Exception) {
                            append(string[0])
                        }
                    }

                    val title = try {
                        it.content.substring(0, it.content.indexOf("\n")).regexLowerCase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    } catch (e: Exception) {
                        it.content.regexLowerCase()
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    }

                    title.replace("god", "God")
                    title.replace("lord", "Lord")
                    title.replace("jesus", "Jesus")
                    title.replace("holy spirit", "Holy Spirit")

                    it.copy(topPick = data.regexLowerCase().replace(" ", ""), title = title)
                }
                db.lyricDao.insert(data)
            }
        }


        ofType = object : TypeToken<List<Other>>() {}.type
        (Gson().fromJson(
            Utils.readJsonFromAssetToString(context, "other.json")!!, ofType
        ) as List<Other>).also { db.otherDao.insert(it) }

        return false
    }


    fun observeHymns(query: String) = db.lyricDao.observeLyrics(query, version)
    val observeCategories = db.lyricDao.observeCategories(version)
    val observeTopPickCategories = db.lyricDao.observeTopPickCategories(version)
    val observeRecentLyrics = db.lyricDao.observeRecentLyrics(version)
    val observeOther = db.otherDao.observeOther()
    val observeFavoriteLyrics = db.lyricDao.observeFavoriteLyrics()

    fun getLyricsById(lyric: Lyric) = db.lyricDao.getLyricsById(lyric.number, version)
    fun findLyricById(id: Int) = db.lyricDao.findLyricById(id, version)
    suspend fun clearFavorite() = db.lyricDao.clearFavorite()


    suspend fun update(lyric: Lyric) = db.lyricDao.update(lyric)
    fun getLyricsByCategory(lyric: Lyric) =
        db.lyricDao.getLyricsByCategory(lyric.categoryId, version)

    suspend fun insert(list: List<Search>) = db.searchDao.insert(list)
    suspend fun delete(list: Search) = db.searchDao.delete(list)
    val observeSearch = db.searchDao.observeSearch()

}