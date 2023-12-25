package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.prefs.Prefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import javax.inject.Inject

@HiltViewModel
class MiscViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val otherRepo: OtherRepository,
  private val prefs: Prefs
) : ViewModel() {

  private val _state = MutableStateFlow(MiscState())
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      lyricRepo.favorites().onEach { favorites ->
        _state.value = _state.value.copy(
          favorites = favorites,
          complementary = otherRepo.query(),
          fontSize = prefs.fontSize
        )
      }.launchIn(this)
    }
  }

  fun onEvent(event: MiscEvent) {
    when (event) {
      is MiscEvent.RemoveFav -> favorite(event.data)
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
    }
}
