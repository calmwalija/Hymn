package net.techandgraphics.hymn.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.asEntity
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.removeSymbols
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchRepo: SearchRepository,
  private val lyricRepo: LyricRepository,
) : ViewModel() {

  private var searchJob: Job? = null
  private val delayDuration = 3L
  private val _state = MutableStateFlow(SearchState())
  val state = _state.asStateFlow()

  private fun queryLyrics() = lyricRepo.query(_state.value.searchQuery)
    .also { _state.value = _state.value.copy(lyricsPaged = it) }
    .cachedIn(viewModelScope)

  init {
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
          _state.value = _state.value.copy(searchQuery = state.value.searchQuery.dropLast(1))
          if (it > 1) clearSearchQuery()
        } catch (_: Exception) {
        }
      }
      onEvent(SearchEvent.OnSearchQuery(state.value.searchQuery))
    }
  }

  private fun searchQueryTag(searchQuery: String) = viewModelScope.launch {
    searchQuery.onEach {
      delay(delayDuration)
      try {
        _state.value = _state.value.copy(searchQuery = state.value.searchQuery + it.toString())
      } catch (_: Exception) {
      }
      onEvent(SearchEvent.OnSearchQuery(state.value.searchQuery))
    }
  }

  fun onEvent(event: SearchEvent) {
    when (event) {
      is SearchEvent.OnSearchQuery -> {
        _state.value = _state.value.copy(searchQuery = event.searchQuery, isSearching = true)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(600)
          queryLyrics()
          delay(delayDuration.times(600))
          _state.value = _state.value.copy(isSearching = false)
        }
      }

      is SearchEvent.SearchQueryTag -> searchQueryTag(event.searchQuery)

      SearchEvent.InsertSearchTag -> onInsertSearchTag()
      SearchEvent.ClearSearchQuery -> clearSearchQuery()
      is SearchEvent.OnLongPress -> onLongPress(event.search)
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
    SearchEntity(query = searchQuery, tag = buildString { searchList.forEach { append(it) } })
      .also { searchRepo.upsert(listOf(it)) }
  }
}
