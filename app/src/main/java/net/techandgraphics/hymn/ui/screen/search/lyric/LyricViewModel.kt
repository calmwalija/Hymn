package net.techandgraphics.hymn.ui.screen.search.lyric

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.removeSymbols
import javax.inject.Inject

@HiltViewModel
class LyricViewModel @Inject constructor(
  private val searchRepo: SearchRepository,
  private val lyricRepo: LyricRepository,
  private val analytics: FirebaseAnalytics,
  private val prefs: DataStorePrefs,
) : ViewModel() {

  private var searchJob: Job? = null
  private val delayDuration = 3L
  private val _state = MutableStateFlow(LyricUiState())
  val state = _state.asStateFlow()

  private fun queryLyrics() = lyricRepo.query(_state.value.searchQuery)
    .onEach { _state.value = _state.value.copy(lyrics = it) }
    .launchIn(viewModelScope)

  init {
    analytics.tagScreen(Tag.SEARCH_SCREEN)
    with(viewModelScope) {
      searchRepo.query()
        .onEach { _state.value = _state.value.copy(search = it.map { it.asModel() }) }
        .launchIn(this)
      queryLyrics()
    }
  }

  private fun clearSearchQuery() {
    viewModelScope.launch {
      delay(delayDuration)
      state.value.searchQuery.length.let {
        try {
          _state.value =
            _state.value.copy(searchQuery = state.value.searchQuery.dropLast(1))
          if (it > 1) clearSearchQuery()
        } catch (_: Exception) {
        }
      }
      onEvent(LyricUiEvent.OnLyricUiQuery(state.value.searchQuery))
    }
  }

  private fun searchQueryTag(searchQuery: String) = viewModelScope.launch {
    searchQuery.onEach {
      delay(delayDuration)
      try {
        _state.value =
          _state.value.copy(searchQuery = state.value.searchQuery + it.toString())
      } catch (_: Exception) {
      }
      onEvent(LyricUiEvent.OnLyricUiQuery(state.value.searchQuery))
    }
  }

  fun onEvent(event: LyricUiEvent) {
    when (event) {
      is LyricUiEvent.OnLyricUiQuery -> {
        _state.value =
          _state.value.copy(searchQuery = event.searchQuery, isSearching = true)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(200)
          queryLyrics()
          delay(delayDuration.times(200))
          _state.value = _state.value.copy(isSearching = false)
        }
      }

      is LyricUiEvent.LyricUiQueryTag -> {
        analytics.tagEvent(
          Tag.APPEND_SEARCH_TAG,
          bundleOf(Pair(Tag.APPEND_SEARCH_TAG, event.searchQuery))
        )
        searchQueryTag(event.searchQuery)
      }

      LyricUiEvent.InsertLyricUiTag -> {
        analytics.tagEvent(
          Tag.SEARCH_KEYWORD,
          bundleOf(Pair(Tag.SEARCH_KEYWORD, state.value.searchQuery))
        )
        onInsertSearchTag()
      }

      LyricUiEvent.ClearLyricUiQuery -> {
        analytics.tagEvent(Tag.CLEAR_SEARCH_TAG, bundleOf())
        clearSearchQuery()
      }

      is LyricUiEvent.OnLongPress -> onLongPress(event.search)
    }
  }

  private fun onLongPress(search: Search) {
    viewModelScope.launch {
      searchRepo.delete(search.asEntity())
    }
  }

  private fun onInsertSearchTag() = viewModelScope.launch {
    val searchQuery = state.value.searchQuery.trim().lowercase()
    val searchList = searchQuery.removeSymbols().split(" ")
    SearchEntity(
      query = searchQuery,
      tag = buildString { searchList.forEach { append(it) } },
      lang = prefs.get(prefs.translationKey, Lang.EN.lowercase()),
    ).also { searchRepo.upsert(listOf(it)) }
  }
}
