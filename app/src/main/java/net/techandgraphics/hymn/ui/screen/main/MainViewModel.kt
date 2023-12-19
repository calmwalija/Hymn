package net.techandgraphics.hymn.ui.screen.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
  private val userPrefs: UserPrefs,
  private val sharedPrefs: SharedPreferences
) : ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()

  init {
    getLangConfig()
    viewModelScope.launch {
      with(database) {
        val queryId = lyricDao.queryId(version)
        _state.value = _state.value.copy(
          queryId = queryId,
          featured = categoryDao.featured(version),
          theHymn = lyricDao.theHymn(version),
          ofTheDay = lyricDao.queryById(queryId),
        )
      }
    }
  }

  private fun getLangConfig() {
    sharedPrefs.getString(
      userPrefs.context.getString(
        R.string.version_key
      ), Lang.EN.name
    )?.let {
      _state.value = _state.value.copy(lang = it)
    }
  }

  private fun languageChange(lang: String, onFinish: () -> Unit) = viewModelScope.launch {
    sharedPrefs.edit().putString(userPrefs.context.getString(R.string.version_key), lang).apply()
    _state.value = _state.value.copy(lang = lang)
    delay(1000)
    onFinish.invoke()
  }

  fun favorite(lyric: LyricEntity) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        database.lyricDao.favorite(favorite, number, version)
      }
      _state.value = _state.value.copy(ofTheDay = database.lyricDao.queryById(state.value.queryId))
    }

  fun onEvent(event: MainEvent) {
    when (event) {
      is MainEvent.Favorite -> favorite(event.data)
      is MainEvent.LanguageChange -> languageChange(event.lang, event.onFinish)
    }
  }
}
