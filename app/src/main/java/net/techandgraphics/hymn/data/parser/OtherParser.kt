package net.techandgraphics.hymn.data.parser

import android.content.Context
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.OtherEntity
import net.techandgraphics.hymn.readJsonFromAssetToString
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OtherParser @Inject constructor(
  private val database: Database,
  private val context: Context,
) {

  private val filename = "other.json"

  private suspend fun readJsonFromAssetToString(onCompleted: suspend (Boolean) -> Unit) {
    withContext(Dispatchers.IO) {
      val ofType: Type = object : TypeToken<List<OtherEntity>>() {}.type
      val file = context.readJsonFromAssetToString(filename) ?: ""
      val json = Gson().fromJson<List<OtherEntity>>(file.ifEmpty { "[]" }, ofType)
      with(database) {
        withTransaction {
          otherDao.clearAll()
          otherDao.upsert(json)
          onCompleted.invoke(json.isEmpty())
        }
      }
    }
  }

  suspend operator fun invoke(event: suspend (Boolean) -> Unit): Boolean {
    readJsonFromAssetToString(event)
    return false
  }
}
