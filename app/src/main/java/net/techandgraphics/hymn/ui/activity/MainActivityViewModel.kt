package net.techandgraphics.hymn.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.parser.LyricParser
import net.techandgraphics.hymn.data.parser.OtherParser
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val lyricParser: LyricParser,
  private val otherParser: OtherParser,
  private val prefs: DataStorePrefs
) : ViewModel() {

  private val _state = MutableStateFlow(MainActivityState())
  val state = _state.asStateFlow()

  init {
    prefs.onInitialize()
  }

  private fun DataStorePrefs.onInitialize() {
    viewModelScope.launch {
      val dynamicColorEnabled = get<Boolean>(dynamicColorKey, true) ?: true
      _state.update { it.copy(dynamicColorEnabled = dynamicColorEnabled) }
      if (get(jsonBuildKey) == DataStorePrefs.JSON_BUILD_KEY) {
        _state.value = _state.value.copy(completed = false)
        return@launch
      }
      lyricParser.invoke {
        otherParser.invoke {
          put(jsonBuildKey, DataStorePrefs.JSON_BUILD_KEY)
          onInitialize()
        }
      }
    }
  }

  fun onEvent(event: MainActivityUiEvent) {
    when (event) {
      is MainActivityUiEvent.DynamicColor -> viewModelScope.launch {
        prefs.get(prefs.dynamicColorKey, event.isEnabled)
        _state.update { it.copy(dynamicColorEnabled = event.isEnabled) }
      }
    }
  }
}
