package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Database
import javax.inject.Inject

@HiltViewModel
class MiscViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
) : ViewModel() {

  private val _state = MutableStateFlow(MiscState())
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      database.lyricDao.favorites(version).onEach { favorites ->
        _state.value = _state.value.copy(
          favorites = favorites,
          complementary = database.essentialDao.query(version)
        )
      }.launchIn(this)
    }
  }
}
