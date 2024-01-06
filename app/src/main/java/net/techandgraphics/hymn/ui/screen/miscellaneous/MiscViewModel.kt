package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.fontSize
import javax.inject.Inject

@HiltViewModel
class MiscViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val otherRepo: OtherRepository,
  private val appPrefs: AppPrefs,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(MiscState())
  val state = _state.asStateFlow()

  init {
    analytics.tagScreen(Tag.MISC_SCREEN)
    viewModelScope.launch {
      lyricRepo.favorites().onEach { favorites ->
        _state.value = _state.value.copy(
          favorites = favorites,
          complementary = otherRepo.query(),
          fontSize = appPrefs.fontSize()
        )
      }.launchIn(this)
    }
  }

  fun onEvent(event: MiscEvent) {
    when (event) {
      is MiscEvent.RemoveFav -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.MISC_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is MiscEvent.OpenCreed -> {
        analytics.tagEvent(
          Tag.OPEN_CREED,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is MiscEvent.OpenFavorite -> {
        analytics.tagEvent(
          Tag.OPEN_FAVORITE,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is MiscEvent.OpenFeedback -> {
        analytics.tagEvent(
          Tag.OPEN_FEEDBACK,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is MiscEvent.OpenLordsPrayer -> {
        analytics.tagEvent(
          Tag.OPEN_LORDS_PRAYER,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is MiscEvent.OpenRating -> {
        analytics.tagEvent(
          Tag.OPEN_RATING,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
    }
}
