package net.techandgraphics.hymn.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.capitaliseWord
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JsonParser @Inject constructor(
  private val db: Database,
  private val context: Context,
) {

  private val lyricRepo = db.lyricDao
  private val filename = "lyrics.json"

  private suspend fun readJsonFromAssetToString(event: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
      val ofType: Type = object : TypeToken<List<LyricEntity>>() {}.type
      val json = Gson().fromJson<List<LyricEntity>>(
        Utils.readJsonFromAssetToString(context, filename)!!,
        ofType
      )
      jsonParser(json, event)
    }
  }

  private suspend fun jsonParser(
    lyrics: List<LyricEntity>,
    event: suspend () -> Unit = {},
    runSearchTag: Boolean = true
  ) {
    val data = lyrics.map {
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
        it.content.substring(0, it.content.indexOf("\n")).regexLowerCase().capitaliseWord()
      } catch (e: Exception) {
        it.content.regexLowerCase().capitaliseWord()
      }

      it.copy(topPick = data.regexLowerCase().replace(" ", ""), title = title)
    }
    lyricRepo.upsert(data)
    if (runSearchTag) db.searchDao.upsert(Constant.searchEntityTags)
    event.invoke()
  }

  suspend operator fun invoke(event: suspend () -> Unit): Boolean {
    readJsonFromAssetToString(event)
    return false
  }
}
