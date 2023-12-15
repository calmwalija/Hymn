package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(
  @ApplicationContext
  context: Context
) {

  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "net.techandgraphics.hymn.data.prefs")
  private val dataStore = context.dataStore

  companion object {
    val build = intPreferencesKey("build_number")
    val currentMills = longPreferencesKey("current_mills")
    val ofTheDay = intPreferencesKey("of_the_day")
  }

  suspend fun setBuild(num: Int) = dataStore.edit { it[build] = num }

  suspend fun mills(mills: Long) = dataStore.edit { it[currentMills] = mills }
  suspend fun ofTheDay(num: Int) = dataStore.edit { it[ofTheDay] = num }

  val getMills = dataStore.data.map { it[currentMills] }
  val getOfTheDay = dataStore.data.map { it[ofTheDay] }
}
