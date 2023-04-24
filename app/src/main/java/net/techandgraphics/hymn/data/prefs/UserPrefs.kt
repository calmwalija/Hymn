package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@Singleton
class UserPrefs @Inject constructor(
  @ApplicationContext context: Context
) {

  private val dataStore =
    context.createDataStore(name = "net.techandgraphics.hymn.data.prefs")

  companion object {
    val build = intPreferencesKey("build_number")
    val whatsNew = intPreferencesKey("whats_new")
    const val BUILD = 3
    const val WHATS_NEW = 2
  }

  suspend fun setBuild(num: Int) = dataStore.edit { it[build] = num }
  suspend fun whatsNew(version: Int) = dataStore.edit { it[whatsNew] = version }

  val getBuild = dataStore.data.map { it[build] ?: 1 }
  val getWhatsNew = dataStore.data.map { it[whatsNew] ?: 1 }

}