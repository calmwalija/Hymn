package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStorePrefs @Inject constructor(
  @ApplicationContext
  val context: Context,
) {

  val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)

  companion object {
    const val JSON_BUILD_KEY = 5.toString()
    const val PREFS_NAME = "net.techandgraphics.hymn.data.prefs"
  }

  val jsonBuildKey = context.getString(R.string.json_build_key)
  val fontKey = context.getString(R.string.font_key)
  val translationKey = context.getString(R.string.translation_key)
  val uniquelyCraftedKey = context.getString(R.string.uniquely_crafted_key)
  val uniquelyCraftedMills = context.getString(R.string.uniquely_crafted_mills_key)
  val dynamicColorKey = context.getString(R.string.dynamic_color)
  val fontStyleKey = context.getString(R.string.font_style_enabled)

  val englishSuggestedForTheWeekKey = "suggestedForTheWeekKey"
  val chichewaSuggestedForTheWeekKey = "chichewaSuggestedForTheWeekKey"

  suspend inline fun <reified T> put(key: String, value: T) {
    context.dataStore.edit {
      when (value) {
        is Int -> it[intPreferencesKey(key)] = value
        is String -> it[stringPreferencesKey(key)] = value
        is Boolean -> it[booleanPreferencesKey(key)] = value
        is Long -> it[longPreferencesKey(key)] = value
        is Float -> it[floatPreferencesKey(key)] = value
      }
    }
  }

  suspend inline fun <reified T> get(key: String, value: T): T? {
    context.dataStore.data.first().let {
      return when (value) {
        is Int -> it[intPreferencesKey(key)] as T?
        is String -> it[stringPreferencesKey(key)] as T?
        is Boolean -> it[booleanPreferencesKey(key)] as T?
        is Long -> it[longPreferencesKey(key)] as T?
        is Float -> it[floatPreferencesKey(key)] as T?
        else -> it[stringPreferencesKey(key)] as T?
      }
    }
  }

  inline fun <reified T> getAsFlow(key: String, value: T): Flow<T?> {
    return context.dataStore.data.map {
      when (value) {
        is Int -> it[intPreferencesKey(key)] as T?
        is String -> it[stringPreferencesKey(key)] as T?
        is Boolean -> it[booleanPreferencesKey(key)] as T?
        is Long -> it[longPreferencesKey(key)] as T?
        is Float -> it[floatPreferencesKey(key)] as T?
        else -> it[stringPreferencesKey(key)] as T?
      }
    }
  }

  suspend fun remove(key: Preferences.Key<*>) = context.dataStore.edit { it.remove(key) }

  suspend fun get(key: String, default: String = ""): String {
    return context.dataStore.data.first()[stringPreferencesKey(key)] ?: default
  }
}
