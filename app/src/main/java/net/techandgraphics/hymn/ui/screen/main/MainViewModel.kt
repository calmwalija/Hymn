package net.techandgraphics.hymn.ui.screen.main

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()
  fun get() = viewModelScope.launch {
    analytics.tagScreen(Tag.MAIN_SCREEN)
    _state.value = _state.value.copy(lang = prefs.get(prefs.translationKey, Lang.EN.lowercase()))
    with(lyricRepo) {
      diveInto().zip(uniquelyCrafted(getLastHymn())) { diveInto, uniquelyCrafted ->
        _state.update {
          it.copy(
            diveInto = diveInto,
            uniquelyCrafted = uniquelyCrafted,
          )
        }
      }.launchIn(viewModelScope)
    }
  }

  private fun languageChange(lang: String) = viewModelScope.launch {
    prefs.put(prefs.translationKey, lang)
    get()
    _state.update { it.copy(lang = lang, onLangInvoke = true) }
    delay(1000)
    _state.update { it.copy(onLangInvoke = false) }
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

  fun onEvent(event: MainEvent) {
    when (event) {
      is MainEvent.Favorite -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.MAIN_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is MainEvent.LanguageChange -> {
        analytics.tagEvent(Tag.BOOK_SWITCH, bundleOf(Pair(Tag.MAIN_SCREEN, event.lang)))
        languageChange(event.lang)
      }
    }
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
