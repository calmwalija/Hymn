package net.techandgraphics.hymn.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        context.createDataStore(name = "net.techandgraphics.hymn.prefs")

    companion object {
        val WhatsNew = booleanPreferencesKey("NEW")
    }

    suspend fun setWhatsNew(isFirstLaunch: Boolean) =
        dataStore.edit { it[WhatsNew] = isFirstLaunch }

    val getWhatsNew = dataStore.data.map { it[WhatsNew] ?: true }

}