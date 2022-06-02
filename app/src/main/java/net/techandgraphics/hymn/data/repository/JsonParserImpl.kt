package net.techandgraphics.hymn.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.capitaliseWord
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.repository.JsonParser
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonParserImpl @Inject constructor(
    val context: Context,
    val db: Database
) : JsonParser {

    override suspend fun fromJson(): Boolean {
        var ofType: Type = object : TypeToken<List<Lyric>>() {}.type
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
                        it.content.substring(0, it.content.indexOf("\n"))
                            .regexLowerCase().capitaliseWord()
                    } catch (e: Exception) {
                        it.content.regexLowerCase().capitaliseWord()
                    }

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
}