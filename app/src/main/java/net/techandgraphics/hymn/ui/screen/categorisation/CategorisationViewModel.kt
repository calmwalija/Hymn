package net.techandgraphics.hymn.ui.screen.categorisation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import javax.inject.Inject

@HiltViewModel
class CategorisationViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
) : ViewModel() {

  private val _state = MutableStateFlow(CategorisationState())
  val state = _state.asStateFlow()

  operator fun invoke(id: Int) = with(id) {
    database.lyricDao.queryByCategory(this, version)
      .zip(database.categoryDao.queryById(this, version)) { lyric, category ->
        _state.value = _state.value.copy(lyric = lyric, category = category)
      }.launchIn(viewModelScope)
  }

  fun favorite(lyric: LyricEntity) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        database.lyricDao.favorite(favorite, number, version)
      }
    }

  fun onEvent(event: CategorisationEvent) {
    when (event) {
      is CategorisationEvent.Favorite -> favorite(event.data)
    }
  }
}
