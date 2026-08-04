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
import net.techandgraphics.hymn.ui.theme.AppTheme
import net.techandgraphics.hymn.ui.theme.FontManager
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val lyricParser: LyricParser,
  private val otherParser: OtherParser,
  private val prefs: DataStorePrefs,
) : ViewModel() {

  private val _state = MutableStateFlow(MainActivityState())
  val state = _state.asStateFlow()

  init {
    prefs.apply {
      onFontStyle()
      onThemeMode()
      onInitialize()
    }
  }

  private fun DataStorePrefs.onFontStyle() = viewModelScope.launch {
    val fontName = get(fontStyleKey, FontManager.Font.Default.font)
      .ifBlank { FontManager.Font.Default.font }
    val fontFamily = FontManager.getFontFamilyFromName(fontName, context)
    _state.update { it.copy(fontFamily = fontFamily) }
  }

  private fun DataStorePrefs.onThemeMode() = viewModelScope.launch {
    val theme = AppTheme.fromStorage(get(appThemeKey, AppTheme.SYSTEM.name))
    _state.update { it.copy(appTheme = theme) }
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
        prefs.put(prefs.dynamicColorKey, event.isEnabled)
        _state.update { it.copy(dynamicColorEnabled = event.isEnabled) }
      }

      is MainActivityUiEvent.FontStyle -> {
        event.fontFamily?.let { family ->
          _state.update { it.copy(fontFamily = family) }
        } ?: prefs.onFontStyle()
      }

      is MainActivityUiEvent.ThemeMode -> viewModelScope.launch {
        prefs.put(prefs.appThemeKey, event.theme.name.lowercase())
        _state.update { it.copy(appTheme = event.theme) }
      }
    }
  }
}
