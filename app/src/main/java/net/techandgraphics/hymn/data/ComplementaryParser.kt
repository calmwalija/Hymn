package net.techandgraphics.hymn.data

import android.content.Context
import androidx.room.withTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.EssentialEntity
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ComplementaryParser @Inject constructor(
  private val database: Database,
  private val context: Context,
) {

  private val filename = "other.json"

  private suspend fun readJsonFromAssetToString(onCompleted: suspend () -> Unit) {
    withContext(Dispatchers.IO) {
      val ofType: Type = object : TypeToken<List<EssentialEntity>>() {}.type
      val json = Gson().fromJson<List<EssentialEntity>>(
        Utils.readJsonFromAssetToString(context, filename)!!,
        ofType
      )
      with(database) {
        withTransaction {
          essentialDao.clearAll()
          essentialDao.upsert(json)
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
