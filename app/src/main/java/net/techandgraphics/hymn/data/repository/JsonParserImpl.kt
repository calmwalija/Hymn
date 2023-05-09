package net.techandgraphics.hymn.data.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.capitaliseWord
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.EssentialEntity
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.domain.repository.JsonParser
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonParserImpl @Inject constructor(
  private val db: Database,
  private val context: Context,
) : JsonParser {

  private val lyricRepo = db.lyricDao
  private val otherRepo = db.essentialDao

  override suspend fun fromJsonToOther() {
    val ofType = object : TypeToken<List<EssentialEntity>>() {}.type
    (
      Gson().fromJson(
        Utils.readJsonFromAssetToString(context, "other.json")!!, ofType
      ) as List<EssentialEntity>
      ).also { otherRepo.insert(it) }
  }

  override suspend fun fromJsonToLyric() {
    val ofType: Type = object : TypeToken<List<LyricEntity>>() {}.type
    (
      Gson().fromJson(
        Utils.readJsonFromAssetToString(context, "lyrics.json")!!, ofType
      ) as List<LyricEntity>
      ).also { fromJsonToLyricImpl(it) }
  }

  private suspend fun fromJsonToLyricImpl(lyric: List<LyricEntity>) {
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
    lyricRepo.insert(data)
    db.searchDao.insert(Constant.searchEntityTags)
  }

  override suspend fun fromJson(): Boolean {
    fromJsonToLyric()
    fromJsonToOther()

    Log.e("TAG", "fromJson: ")
    return false
  }
}
