package net.techandgraphics.hymn.prefs

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        context.createDataStore(name = "net.techandgraphics.hymn.prefs")

    companion object {
        val SORT = stringPreferencesKey("SORT")
        val TYPE = stringPreferencesKey("TYPE")
    }


}