package net.techandgraphics.hymn.ui.screen.theCategory

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import javax.inject.Inject

@HiltViewModel
class TheCategoryViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val categoryRepo: CategoryRepository,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(TheCategoryState())
  val state = _state.asStateFlow()

  operator fun invoke(id: Int) = with(id) {
    analytics.tagScreen(Tag.CATEGORISATION_SCREEN)
    lyricRepo.queryByCategory(this)
      .zip(categoryRepo.queryById(this)) { lyric, category ->
        _state.value = _state.value.copy(lyric = lyric, category = category)
      }.launchIn(viewModelScope)
  }

  fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
    }

  fun onEvent(event: TheCategoryUiEvent) {
    when (event) {
      is TheCategoryUiEvent.Favorite -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.CATEGORISATION_SCREEN, state.value.lyric.first().title))
        )
        favorite(event.data)
      }
    }
  }
}
