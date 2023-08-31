package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.timeInMillisMonth
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
    val whatsNew = intPreferencesKey("whats_new")
    val currentMills = longPreferencesKey("current_mills")
    val ofTheDay = intPreferencesKey("of_the_day")
    val donatePeriod = longPreferencesKey("donate_period")
    val onBoarding = booleanPreferencesKey("on_boarding")
    const val BUILD = 3
    const val WHATS_NEW = 2
  }

  suspend fun setBuild(num: Int) = dataStore.edit { it[build] = num }
  suspend fun whatsNew(version: Int) = dataStore.edit { it[whatsNew] = version }
  suspend fun mills(mills: Long) = dataStore.edit { it[currentMills] = mills }
  suspend fun ofTheDay(num: Int) = dataStore.edit { it[ofTheDay] = num }
  suspend fun donatePeriod(mills: Long) = dataStore.edit { it[donatePeriod] = mills }
  suspend fun onBoarding(boolean: Boolean) = dataStore.edit { it[onBoarding] = boolean }

  val getBuild = dataStore.data.map { it[build] ?: 1 }
  val getMills = dataStore.data.map { it[currentMills] }
  val getOfTheDay = dataStore.data.map { it[ofTheDay] }
  val getWhatsNew = dataStore.data.map { it[whatsNew] ?: 1 }
  val getOnBoarding = dataStore.data.map { it[onBoarding] ?: false }
  val getDonatePeriod = dataStore.data.map { it[donatePeriod] ?: timeInMillisMonth() }
}
