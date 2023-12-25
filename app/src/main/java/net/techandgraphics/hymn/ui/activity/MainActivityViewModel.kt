package net.techandgraphics.hymn.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.data.parser.ComplementaryParser
import net.techandgraphics.hymn.data.parser.JsonParser
import net.techandgraphics.hymn.data.prefs.AppPrefs
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
  private val jsonParser: JsonParser,
  private val complementaryParser: ComplementaryParser,
  private val appPrefs: AppPrefs
) : ViewModel() {

  private val _state = MutableStateFlow(MainActivityState())
  val state = _state.asStateFlow()

  init {
    appPrefs.getJsonBuild.onEach { jsonBuild ->
      if (jsonBuild == AppPrefs.JSON_BUILD) {
        _state.value = _state.value.copy(completed = true)
        return@onEach
      }
      jsonParser.invoke {
        complementaryParser.invoke {
          appPrefs.setJsonBuild(AppPrefs.JSON_BUILD)
        }
      }
    }.launchIn(viewModelScope)
  }
}
