package net.techandgraphics.hymn.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.data.local.Database
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  database: Database,
  version: String,
) : ViewModel() {

  private val lyric = database.lyricDao.query(version = version)
  private val search = database.searchDao.query(version = version)
  private val _state = MutableStateFlow(SearchState())
  val state = _state.asStateFlow()

  init {
    lyric.onEach { _state.value = _state.value.copy(lyric = it) }.launchIn(viewModelScope)
    search.onEach { _state.value = _state.value.copy(search = it) }.launchIn(viewModelScope)
  }
}
