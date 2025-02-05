package net.techandgraphics.hymn.ui.activity

import android.graphics.Typeface
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.preferences.core.stringSetPreferencesKey
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
import net.techandgraphics.hymn.fontFile
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
    prefs.apply { onFontStyle(); onInitialize() }
  }

  private fun DataStorePrefs.onFontStyle() = viewModelScope.launch {
    val isFontAvailable = get<String?>(fontStyleKey, null)
    val fontFamily = if (isFontAvailable != null) try {
      FontFamily(Typeface.createFromFile(prefs.context.fontFile()))
    } catch (e: Exception) {
      remove(stringSetPreferencesKey(fontStyleKey))
      FontFamily.Default
    } else FontFamily.Default
    _state.update { it.copy(fontFamily = fontFamily) }
  }

  private fun DataStorePrefs.onInitialize() {
    viewModelScope.launch {
      val dynamicColorEnabled = get<Boolean>(dynamicColorKey, true) ?: true
      _state.update { it.copy(dynamicColorEnabled = dynamicColorEnabled) }
      if (get(jsonBuildKey) == DataStorePrefs.JSON_BUILD_KEY) {
        _state.value = _state.value.copy(completed = false, showStartupFailure = false)
        return@launch
      }
      var lyricIsEmpty = false
      var otherIsEmpty = false
      lyricParser.invoke { lyricIsEmpty = it; otherParser.invoke { otherIsEmpty = it } }
      if (lyricIsEmpty || otherIsEmpty) {
        _state.value = _state.value.copy(completed = false, showStartupFailure = true)
        return@launch
      }
      put(jsonBuildKey, DataStorePrefs.JSON_BUILD_KEY)
      onInitialize()
    }
  }

  fun onEvent(event: MainActivityUiEvent) {
    when (event) {
      is MainActivityUiEvent.DynamicColor -> viewModelScope.launch {
        prefs.get(prefs.dynamicColorKey, event.isEnabled)
        _state.update { it.copy(dynamicColorEnabled = event.isEnabled) }
      }

      is MainActivityUiEvent.FontStyle -> prefs.onFontStyle()
    }
  }
}
