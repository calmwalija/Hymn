package net.techandgraphics.hymn.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.data.prefs.SharedPrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val categoryRepo: CategoryRepository,
  private val prefs: SharedPrefs,
  appPrefs: AppPrefs
) : ViewModel() {

  private val _state = MutableStateFlow(MainState())
  val state = _state.asStateFlow()

  init {
    _state.value = _state.value.copy(lang = prefs.lang)
    appPrefs.getPrefs(appPrefs.jsonBuildKey).onEach { readyKey ->
      if (readyKey == null || readyKey == false.toString()) return@onEach
      with(lyricRepo) {
        _state.value = _state.value.copy(spotlight = categoryRepo.spotlight())
        queryId()?.let {
          diveInto().zip(queryById(it)) { diveInto, uniquelyCrafted ->
            _state.value = _state.value.copy(
              queryId = it,
              diveInto = diveInto,
              uniquelyCrafted = uniquelyCrafted,
            )
          }.launchIn(viewModelScope)
        }
      }
    }.launchIn(viewModelScope)
  }

  private fun languageChange(lang: String, onFinish: () -> Unit) = viewModelScope.launch {
    prefs.setLang(lang)
    _state.value = _state.value.copy(lang = lang)
    delay(1000)
    onFinish.invoke()
  }

  fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
      lyricRepo.queryById(state.value.queryId).onEach {
        _state.value = _state.value.copy(uniquelyCrafted = it)
      }.launchIn(this)
    }

  fun onEvent(event: MainEvent) {
    when (event) {
      is MainEvent.Favorite -> favorite(event.data)
      is MainEvent.LanguageChange -> languageChange(event.lang, event.onFinish)
    }
  }
}
