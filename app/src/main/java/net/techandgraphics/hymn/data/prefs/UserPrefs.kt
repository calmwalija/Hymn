package net.techandgraphics.hymn.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        context.createDataStore(name = "net.techandgraphics.hymn.data.prefs")

    companion object {
        val build = intPreferencesKey("build_number")
        const val BUILD = 1
    }

    suspend fun setBuild(num: Int) = dataStore.edit { it[build] = num }

    val getBuild = dataStore.data.map { it[build] ?: 0 }

}