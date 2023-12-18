package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.ui.screen.search.searchFilters
import net.techandgraphics.hymn.ui.screen.search.searchOrders
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

    val filterBy = stringPreferencesKey("filter_by")
    val sortBy = stringPreferencesKey("sort_by")
  }

  suspend fun setBuild(num: Int) = dataStore.edit { it[build] = num }
  suspend fun setSortBy(sort: String) = dataStore.edit { it[sortBy] = sort }
  suspend fun setFilterBy(filter: String) = dataStore.edit { it[filterBy] = filter }

  suspend fun mills(mills: Long) = dataStore.edit { it[currentMills] = mills }
  suspend fun ofTheDay(num: Int) = dataStore.edit { it[ofTheDay] = num }

  val getMills = dataStore.data.map { it[currentMills] }
  val getOfTheDay = dataStore.data.map { it[ofTheDay] }

  val getSortBy = dataStore.data.map { it[sortBy] ?: searchOrders.first() }
  val getFilterBy = dataStore.data.map { it[filterBy] ?: searchFilters.first() }
}
