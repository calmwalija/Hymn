package net.techandgraphics.hymn.ui.screen.categorisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

@HiltViewModel
class CategorisationViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val categoryRepo: CategoryRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(CategorisationState())
  val state = _state.asStateFlow()

  operator fun invoke(id: Int) = with(id) {
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

  fun onEvent(event: CategorisationEvent) {
    when (event) {
      is CategorisationEvent.Favorite -> favorite(event.data)
    }
  }
}
