package net.techandgraphics.hymn.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.JsonParser
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val jsonParser: JsonParser) : ViewModel() {

  fun init() = viewModelScope.launch {
    jsonParser.invoke { }
  }
}
