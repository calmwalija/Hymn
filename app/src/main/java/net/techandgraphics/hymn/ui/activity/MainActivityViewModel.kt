package net.techandgraphics.hymn.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.data.parser.LyricParser
import net.techandgraphics.hymn.data.parser.OtherParser
import net.techandgraphics.hymn.data.prefs.AppPrefs
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val lyricParser: LyricParser,
  private val otherParser: OtherParser,
  private val appPrefs: AppPrefs
) : ViewModel() {

  private val _state = MutableStateFlow(MainActivityState())
  val state = _state.asStateFlow()

  init {
    appPrefs.getPrefs(appPrefs.jsonBuildKey).onEach { jsonBuildKey ->
      if (jsonBuildKey != null)
        if (jsonBuildKey == AppPrefs.JSON_BUILD_KEY) {
          _state.value = _state.value.copy(completed = true)
          return@onEach
        }
      lyricParser.invoke {
        otherParser.invoke {
          appPrefs.setPrefs(appPrefs.jsonBuildKey, AppPrefs.JSON_BUILD_KEY)
        }
      }
    }.launchIn(viewModelScope)
  }
}
