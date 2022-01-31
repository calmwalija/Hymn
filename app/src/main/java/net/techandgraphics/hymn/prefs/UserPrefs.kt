package net.techandgraphics.hymn.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.ui.fragments.main.MainFragment
import net.techandgraphics.hymn.ui.fragments.main.MainFragment.Companion.SortBy
import javax.inject.Inject

class UserPrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val dataStore =
        context.createDataStore(name = "net.techandgraphics.hymn.prefs")

    companion object {
        val SORT = stringPreferencesKey("SORT")
     }


    suspend fun setSort(sortBy: String) =
        dataStore.edit { it[SORT] = sortBy }

    val getSort = dataStore.data.map { it[SORT] ?: SortBy.NAME.name }

}