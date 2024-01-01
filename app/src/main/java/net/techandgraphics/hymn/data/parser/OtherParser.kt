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

  private suspend fun readJsonFromAssetToString(onCompleted: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
      val ofType: Type = object : TypeToken<List<OtherEntity>>() {}.type
      val json = Gson().fromJson<List<OtherEntity>>(
        context readJsonFromAssetToString filename,
        ofType
      )
      with(database) {
        withTransaction {
          otherDao.clearAll()
          otherDao.upsert(json)
          onCompleted.invoke()
        }
      }
    }
  }

  suspend operator fun invoke(event: suspend () -> Unit): Boolean {
    readJsonFromAssetToString(event)
    return false
  }
}
