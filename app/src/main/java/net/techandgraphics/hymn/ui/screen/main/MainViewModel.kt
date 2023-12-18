package net.techandgraphics.hymn.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
  private val userPrefs: UserPrefs
) : ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()

  init {
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
    }
  }
}
