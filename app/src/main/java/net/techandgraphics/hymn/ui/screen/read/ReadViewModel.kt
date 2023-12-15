package net.techandgraphics.hymn.ui.screen.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
) : ViewModel() {

  private val _state = MutableStateFlow(ReadState())
  val state = _state.asStateFlow()

  operator fun invoke(id: Int) = with(id) {
    database.lyricDao.queryById(this, version).onEach {
      _state.value = _state.value.copy(lyrics = it)
      if (it.isNotEmpty()) {
        timestamp(it.first())
//        topPickHit(it.first())
      }
    }.launchIn(viewModelScope)
  }

  private fun timestamp(data: LyricEntity) = viewModelScope.launch {
    database.lyricDao.upsert(listOf(data.copy(timestamp = System.currentTimeMillis())))
  }

  private fun topPickHit(data: LyricEntity) = viewModelScope.launch {
    database.lyricDao.upsert(listOf(data.copy(topPickHit = data.topPickHit.plus(1))))
  }
}
