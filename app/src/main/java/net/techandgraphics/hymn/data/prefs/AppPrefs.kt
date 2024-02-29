package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPrefs @Inject constructor(
  @ApplicationContext
  val context: Context
) {

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "net.techandgraphics.hymn.data.prefs")
  private val dataStore = context.dataStore

  companion object {
    const val JSON_BUILD_KEY = 5.toString()
  }

  val jsonBuildKey = context.getString(R.string.json_build_key)
  val fontKey = context.getString(R.string.font_key)
  val readyKey = context.getString(R.string.ready_key)

  suspend fun setPrefs(key: String, value: String) {
    dataStore.edit { it[stringPreferencesKey(key)] = value }
  }

  fun getPrefs(key: String): Flow<String?> {
    return dataStore.data.map { it[stringPreferencesKey(key)] }
  }
}
