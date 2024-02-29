package net.techandgraphics.hymn.data.parser

import android.content.Context
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.capitaliseWord
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.readJsonFromAssetToString
import net.techandgraphics.hymn.removeSymbols
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LyricParser @Inject constructor(
  private val database: Database,
  private val context: Context,
) {

  private val dao = database.lyricDao
  private val searchDao = database.searchDao
  private val filename = "lyrics.json"

  private suspend fun readJsonFromAssetToString(event: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
      val ofType: Type = object : TypeToken<List<LyricEntity>>() {}.type
      val json = Gson().fromJson<List<LyricEntity>>(
        context readJsonFromAssetToString filename,
        ofType
      )
      jsonParser(json, event)
    }
  }

  private suspend fun jsonParser(
    lyrics: List<LyricEntity>,
    event: suspend () -> Unit = {},
  ) {
    val data = lyrics.map {
      val title = try {
        it.content.substring(0, it.content.indexOf("\n")).removeSymbols().capitaliseWord()
      } catch (e: Exception) {
        it.content.removeSymbols().capitaliseWord()
      }

      it.copy(title = title)
    }
    database.withTransaction {
      with(dao.backup()) {
        dao.upsert(data)
        dao.upsert(this)
        searchDao.clear()
        searchDao.upsert(Constant.searchEntityTags)
      }
    }
    event.invoke()
  }

  suspend operator fun invoke(event: suspend () -> Unit) {
    readJsonFromAssetToString(event)
  }
}
