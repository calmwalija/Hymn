package net.techandgraphics.hymn.ui.screen.main

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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.combined
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.removeSymbols
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics,
  private val searchRepo: SearchRepository,
  private val categoryRepo: CategoryRepository,
  private val otherRepo: OtherRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(MainUiState())
  val state: StateFlow<MainUiState> = _state.asStateFlow()
  private var searchJob: Job? = null
  private val delayDuration = 3L
  private val channel = Channel<MainChannelEvent>()
  val channelFlow = channel.receiveAsFlow()

  init {
    prepareData()
  }

  private fun prepareData() = viewModelScope.launch {
    analytics.tagScreen(Tag.MAIN_SCREEN)
    prefs.getAsFlow<String>(prefs.jsonBuildKey, "")
      .filterNotNull()
      .onEach {
        _state.update {
          it.copy(
            translation = prefs.get(prefs.translationKey, Translation.EN.lowercase()),
            uniquelyCrafted = lyricRepo.uniquelyCrafted(),
            emptyStateSuggestedLyrics = lyricRepo.emptyStateSuggested(),
            categories = categoryRepo.query(_state.value.searchQuery),
            theCreedAndLordsPrayer = otherRepo.query(),
            fontSize = prefs.get(prefs.fontKey, 1.toString()).toInt()
          )
        }
      }.launchIn(viewModelScope)
    observeData()
  }

  private fun onQueryChange() = lyricRepo.query(_state.value.searchQuery.trim())
    .onEach { _state.value = _state.value.copy(lyrics = it) }
    .launchIn(viewModelScope)

  private fun observeData() = combine(
    lyricRepo.query(_state.value.searchQuery.trim()),
    searchRepo.query().map { it.map { search -> search.asModel() } },
    lyricRepo.diveInto(),
    lyricRepo.favorites()
  ) { lyrics, search, diveInto, favorites ->
    _state.update {
      it.copy(
        lyrics = lyrics,
        search = search,
        diveInto = diveInto,
        favorites = favorites,
      )
    }
  }.launchIn(viewModelScope)

  private fun languageChange(lang: String) = viewModelScope.launch {
    prefs.put(prefs.translationKey, lang)
    _state.update { it.copy(translation = lang) }
    prepareData()
    channel.send(MainChannelEvent.Language)
  }

  fun onEvent(event: MainUiEvent) {
    when (event) {

      is MainUiEvent.ChangeTranslation -> {
        analytics.tagEvent(Tag.TRANSLATION_OPTION, Pair(Tag.MAIN_SCREEN, event.lang))
        languageChange(event.lang)
      }

      MainUiEvent.FeaturedCategories ->
        analytics.tagScreen(Tag.CATEGORY_SCREEN)

      MainUiEvent.LyricEvent.ClearSearchQuery -> {
        analytics.tagEvent(
          Tag.CLEAR_SEARCH_TAG,
          Pair(Tag.APPEND_SEARCH_TAG, state.value.searchQuery.trim())
        )
        clearSearchQuery()
      }

      MainUiEvent.LyricEvent.InsertSearchTag -> {
        analytics.tagEvent(Tag.SEARCH_KEYWORD, Pair(Tag.SEARCH_KEYWORD, state.value.searchQuery))
        onInsertSearchTag()
      }

      is MainUiEvent.LyricEvent.QueryTag -> {
        analytics.tagEvent(Tag.APPEND_SEARCH_TAG, Pair(Tag.APPEND_SEARCH_TAG, event.searchQuery))
        searchTag(event.searchQuery)
      }

      is MainUiEvent.LyricEvent.LyricSearch -> {
        _state.value =
          _state.value.copy(searchQuery = event.searchQuery, isSearching = true)
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(200)
          onQueryChange()
          delay(delayDuration.times(200))
          _state.value = _state.value.copy(isSearching = false)
        }
      }

      is MainUiEvent.AnalyticEvent -> onAnalyticEvent(event)

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
      onEvent(MainUiEvent.LyricEvent.LyricSearch(state.value.searchQuery))
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
      onEvent(MainUiEvent.LyricEvent.LyricSearch(state.value.searchQuery))
    }
  }

  private fun onInsertSearchTag() = viewModelScope.launch {
    if (state.value.lyrics.isEmpty()) return@launch
    val searchQuery = state.value.searchQuery.trim().lowercase()
    val searchList = searchQuery.removeSymbols().split(" ")
    SearchEntity(
      query = searchQuery,
      tag = buildString { searchList.forEach { append(it) } },
      lang = prefs.get(prefs.translationKey, Translation.EN.lowercase()),
    ).also { searchRepo.upsert(listOf(it)) }
  }

  private fun onAnalyticEvent(event: MainUiEvent.AnalyticEvent) {
    when (event) {

      is MainUiEvent.AnalyticEvent.KeyboardType ->
        analytics.tagEvent(Tag.KEYBOARD_TYPE, Pair(Tag.KEYBOARD_TYPE, event.keyboardType))

      is MainUiEvent.AnalyticEvent.GotoPreviewFromDiveInto ->
        analytics.tagEvent(
          Tag.GOTO_PREVIEW_FROM_DIVE_INTO,
          Pair(Tag.GOTO_PREVIEW_FROM_DIVE_INTO, event.theNumber),
        )

      is MainUiEvent.AnalyticEvent.GotoPreviewFromFavorite ->
        analytics.combined(
          Tag.GOTO_PREVIEW_FROM_FAVORITE,
          Pair(Tag.HYMN_TITLE, event.lyric.title),
          Pair(Tag.HYMN_NUMBER, event.lyric.number),
        )

      is MainUiEvent.AnalyticEvent.GotoPreviewFromSearch ->
        analytics.combined(
          Tag.GOTO_PREVIEW_FROM_SEARCH,
          Pair(Tag.HYMN_TITLE, event.lyric.title),
          Pair(Tag.HYMN_NUMBER, event.lyric.number),
        )

      is MainUiEvent.AnalyticEvent.GotoPreviewFromUniquelyCrafted ->
        analytics.combined(
          Tag.GOTO_PREVIEW_FROM_UNIQUE_CRAFTED,
          Pair(Tag.HYMN_TITLE, event.lyric.title),
          Pair(Tag.HYMN_NUMBER, event.lyric.number),
        )

      is MainUiEvent.AnalyticEvent.GotoTheCategory ->
        analytics.combined(
          Tag.GOTO_THE_CATEGORY,
          Pair(Tag.GOTO_THE_CATEGORY, event.category.lyric.categoryName),
          Pair(Tag.GOTO_THE_CATEGORY, event.category.lyric.categoryId),
        )

      is MainUiEvent.AnalyticEvent.SearchEmptyState ->
        analytics.tagEvent(
          Tag.SEARCH_EMPTY_STATE,
          Pair(Tag.SEARCH_EMPTY_STATE, event.keyword),
        )

      MainUiEvent.AnalyticEvent.GotoSettingScreen ->
        analytics.tagEvent(
          Tag.GOTO_SETTING_SCREEN,
          Pair(Tag.TRANSLATION_DEFAULT, state.value.translation),
        )

      MainUiEvent.AnalyticEvent.ShowApostlesCreedDialog ->
        analytics.tagEvent(
          Tag.SHOW_APOSTLES_CREED_DIALOG,
          Pair(Tag.SHOW_APOSTLES_CREED_DIALOG, true)
        )

      MainUiEvent.AnalyticEvent.ShowFavoriteDialog ->
        analytics.tagEvent(Tag.SHOW_FAVORITE_DIALOG, Pair(Tag.SHOW_FAVORITE_DIALOG, true))

      MainUiEvent.AnalyticEvent.ShowFeaturedCategoriesDialog ->
        analytics.tagEvent(
          Tag.SHOW_FEATURED_CATEGORIES_DIALOG,
          Pair(Tag.SHOW_FEATURED_CATEGORIES_DIALOG, true)
        )

      MainUiEvent.AnalyticEvent.ShowLordsPrayerDialog ->
        analytics.tagEvent(Tag.SHOW_LORDS_PRAYER_DIALOG, Pair(Tag.SHOW_LORDS_PRAYER_DIALOG, true))

      MainUiEvent.AnalyticEvent.ShowMenuDialog ->
        analytics.tagEvent(Tag.SHOW_MENU_DIALOG, Pair(Tag.SHOW_MENU_DIALOG, true))

      MainUiEvent.AnalyticEvent.ShowTranslationDialog ->
        analytics.tagEvent(Tag.SHOW_TRANSLATION_DIALOG, Pair(Tag.SHOW_TRANSLATION_DIALOG, true))
    }
  }
}
