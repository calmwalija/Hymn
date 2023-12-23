package net.techandgraphics.hymn.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.removeSymbols
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
  private val prefs: UserPrefs
) : ViewModel() {

  private var searchJob: Job? = null
  private val delayDuration = 3L
  private val search = database.searchDao.query(version = version)
  private val _state = MutableStateFlow(SearchState())
  val state = _state.asStateFlow()
  private val pageSize = 20

  private fun queryLyrics() = Pager(
    config = PagingConfig(
      pageSize = pageSize,
      maxSize = pageSize.times(3)
    ),
    pagingSourceFactory = { database.lyricDao.query(_state.value.searchQuery, version) }
  )
    .flow
    .also { _state.value = _state.value.copy(lyricsPaged = it) }
    .cachedIn(viewModelScope)

  init {
    with(viewModelScope) {
      search.onEach { _state.value = _state.value.copy(search = it) }.launchIn(this)
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
      is SearchEvent.FilterBy -> viewModelScope.launch { prefs.setFilterBy(event.filter) }
      is SearchEvent.SortBy -> viewModelScope.launch { prefs.setSortBy(event.sort) }
    }
  }

  private fun onInsertSearchTag() = viewModelScope.launch {
    val searchQuery = state.value.searchQuery.trim().lowercase()
    val searchList = searchQuery.removeSymbols().split(" ")
    SearchEntity(query = searchQuery, tag = buildString { searchList.forEach { append(it) } })
      .also { database.searchDao.upsert(listOf(it)) }
  }
}
