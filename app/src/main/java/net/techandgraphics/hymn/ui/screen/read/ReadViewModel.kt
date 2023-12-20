package net.techandgraphics.hymn.ui.screen.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

  operator fun invoke(id: Int) = viewModelScope.launch {
    var index = 1
    database.lyricDao.queryByNumber(id, version)
      .map { lyric ->
        LyricEntityKey(
          key = if (lyric.chorus == 0) (index++).toString() else "Chorus",
          lyric = lyric
        )
      }.also {
        _state.value = _state.value.copy(lyricEntityKey = it)
      }
  }

  private fun read(data: LyricEntity) = with(data) {
    viewModelScope.launch { database.lyricDao.read(number, topPickHit + 1, version = version) }
  }

  fun favorite(lyric: LyricEntity) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        database.lyricDao.favorite(favorite, number, version)
      }
      invoke(lyric.number)
    }

  fun onEvent(event: ReadEvent) {
    when (event) {
      is ReadEvent.Favorite -> favorite(event.data)
      is ReadEvent.Click -> Unit
      is ReadEvent.Read -> read(event.data)
    }
  }

  private fun timestamp(data: LyricEntity) = viewModelScope.launch {
    database.lyricDao.upsert(listOf(data.copy(timestamp = System.currentTimeMillis())))
  }

  private fun topPickHit(data: LyricEntity) = viewModelScope.launch {
    database.lyricDao.upsert(listOf(data.copy(topPickHit = data.topPickHit.plus(1))))
  }
}
