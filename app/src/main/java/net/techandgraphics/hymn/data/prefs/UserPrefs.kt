package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(
  @ApplicationContext
  val context: Context
) {

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "net.techandgraphics.hymn.data.prefs")
  private val dataStore = context.dataStore

  companion object {
    const val JSON_BUILD = 2

    val jsonBuild = intPreferencesKey("json_build")
  }

  suspend fun setJsonBuild(build: Int) = dataStore.edit { it[jsonBuild] = build }
  val getJsonBuild = dataStore.data.map { it[jsonBuild] ?: 1 }
}
