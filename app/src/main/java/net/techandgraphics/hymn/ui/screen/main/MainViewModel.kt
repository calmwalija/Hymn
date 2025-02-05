package net.techandgraphics.hymn.ui.screen.main

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.removeSymbols
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent.Favorite
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent.Language
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics,
  private val searchRepo: SearchRepository,
  private val categoryRepo: CategoryRepository,

) : ViewModel() {

  private val _state = MutableStateFlow(MainUiState())
  val state: StateFlow<MainUiState> = _state.asStateFlow()
  private var searchJob: Job? = null
  private val delayDuration = 3L
  private val channel = Channel<MainChannelEvent>()
  val channelFlow = channel.receiveAsFlow()

  fun get() = viewModelScope.launch {
    analytics.tagScreen(Tag.MAIN_SCREEN)
    _state.update { it.copy(lang = prefs.get(prefs.translationKey, Lang.EN.lowercase())) }
    _state.update { it.copy(diveInto = lyricRepo.diveInto()) }
    _state.update { it.copy(uniquelyCrafted = lyricRepo.uniquelyCrafted()) }
    queryLyrics()
    queryCategories()
    querySearch()
    queryFavorites()
    emptyStateSuggestedLyrics()
  }

  private fun emptyStateSuggestedLyrics() = viewModelScope.launch {
    _state.update { it.copy(emptyStateSuggestedLyrics = lyricRepo.emptyStateSuggested()) }
  }

  private fun querySearch() = searchRepo.query()
    .onEach { _state.value = _state.value.copy(search = it.map { it.asModel() }) }
    .launchIn(viewModelScope)

  private fun queryCategories() = categoryRepo.query(_state.value.searchQuery)
    .onEach { _state.value = _state.value.copy(categories = it) }
    .launchIn(viewModelScope)

  private fun queryFavorites() = lyricRepo.favorites()
    .onEach { _state.value = _state.value.copy(favorites = it) }
    .launchIn(viewModelScope)

  private fun languageChange(lang: String) = viewModelScope.launch {
    prefs.put(prefs.translationKey, lang)
    _state.update { it.copy(lang = lang) }
    get()
    channel.send(MainChannelEvent.Language)
  }

  fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
      lyricRepo.queryById(lyric.lyricId).onEach {
        _state.value = _state.value.copy(uniquelyCrafted = it)
      }.launchIn(this)
    }

  fun onEvent(event: MainUiEvent) {
    when (event) {
      is Favorite -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.MAIN_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is Language -> {
        analytics.tagEvent(Tag.BOOK_SWITCH, bundleOf(Pair(Tag.MAIN_SCREEN, event.lang)))
        languageChange(event.lang)
      }

      MainUiEvent.CategoryUiEvent.OnViewCategories ->
        analytics.tagScreen(Tag.CATEGORY_SCREEN)

      MainUiEvent.LyricUiEvent.ClearLyricUiQuery -> {
        analytics.tagEvent(Tag.CLEAR_SEARCH_TAG, bundleOf())
        clearSearchQuery()
      }

      MainUiEvent.LyricUiEvent.InsertLyricUiTag -> {
        analytics.tagEvent(
          Tag.SEARCH_KEYWORD,
          bundleOf(Pair(Tag.SEARCH_KEYWORD, state.value.searchQuery))
        )
        onInsertSearchTag()
      }

      is MainUiEvent.LyricUiEvent.LyricUiQueryTag -> {
        analytics.tagEvent(
          Tag.APPEND_SEARCH_TAG,
          bundleOf(Pair(Tag.APPEND_SEARCH_TAG, event.searchQuery))
        )
        searchTag(event.searchQuery)
      }

      is MainUiEvent.LyricUiEvent.OnLongPress -> {
      }

      is MainUiEvent.LyricUiEvent.OnLyricUiQuery -> {
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

      else -> Unit
    }
  }

  private fun searchTag(searchQuery: String) = viewModelScope.launch {
    searchQuery.onEach {
      delay(delayDuration)
      try {
        _state.value =
          _state.value.copy(searchQuery = state.value.searchQuery + it.toString())
      } catch (_: Exception) {
      }
      onEvent(MainUiEvent.LyricUiEvent.OnLyricUiQuery(state.value.searchQuery))
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
      onEvent(MainUiEvent.LyricUiEvent.OnLyricUiQuery(state.value.searchQuery))
    }
  }

  private fun queryLyrics() = lyricRepo.query(_state.value.searchQuery.trim())
    .onEach { _state.value = _state.value.copy(lyrics = it) }
    .launchIn(viewModelScope)

  private fun onInsertSearchTag() = viewModelScope.launch {
    if (state.value.lyrics.isEmpty()) return@launch
    val searchQuery = state.value.searchQuery.trim().lowercase()
    val searchList = searchQuery.removeSymbols().split(" ")
    SearchEntity(
      query = searchQuery,
      tag = buildString { searchList.forEach { append(it) } },
      lang = prefs.get(prefs.translationKey, Lang.EN.lowercase()),
    ).also { searchRepo.upsert(listOf(it)) }
  }

  fun onAnalyticEvent(event: AnalyticEvent) {
    when (event) {
      AnalyticEvent.GotoCategory -> {
        analytics.tagEvent(Tag.CATEGORY_SCREEN, bundleOf(Pair(Tag.MAIN_SCREEN, null)))
      }

      AnalyticEvent.GotoSearch -> {
        analytics.tagEvent(Tag.SEARCH_SCREEN, bundleOf(Pair(Tag.MAIN_SCREEN, null)))
      }

      is AnalyticEvent.DiveInto -> {
        analytics.tagEvent(Tag.DIVE_INTO, bundleOf(Pair(Tag.MAIN_SCREEN, event.number)))
      }

      is AnalyticEvent.Spotlight -> {
        analytics.tagEvent(Tag.SPOTLIGHT, bundleOf(Pair(Tag.MAIN_SCREEN, event.categoryId)))
      }
    }
  }
}
