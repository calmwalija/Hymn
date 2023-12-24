package net.techandgraphics.hymn.ui.screen.read

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.asTimestamp
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
  private val sharedPreferences: SharedPreferences,
  private val userPrefs: UserPrefs,
) : ViewModel() {

  private val _state = MutableStateFlow(ReadState())
  val state = _state.asStateFlow()

  private var fontJob: Job? = null

  operator fun invoke(id: Int, timestamp: Boolean = true) = viewModelScope.launch {
    var index = 1
    database.lyricDao.queryByNumber(id, version)
      .map { lyric ->
        LyricEntityKey(
          key = if (lyric.chorus == 0) (index++).toString() else "Chorus",
          lyric = lyric
        )
      }.also {
        _state.value = _state.value.copy(
          lyricEntityKey = it,
          fontSize = sharedPreferences
            .getInt(userPrefs.context.getString(R.string.font_key), 2)
        )
        if (timestamp) onEvent(ReadEvent.Read(it.first().lyric))
      }
  }

  private fun read(data: LyricEntity) = with(data) {
    viewModelScope.launch {
      database.lyricDao.read(number, version = version)
      database.timestampDao.upsert(listOf(data.asTimestamp()))
    }
  }

  fun favorite(lyric: LyricEntity) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        database.lyricDao.favorite(favorite, number, version)
      }
      invoke(lyric.number, false)
    }

  fun onEvent(event: ReadEvent) {
    when (event) {
      is ReadEvent.Favorite -> favorite(event.data)
      is ReadEvent.Click -> Unit
      is ReadEvent.Read -> read(event.data)
      is ReadEvent.FontSize -> fontSize(event.size)
    }
  }

  private fun fontSize(font: Int) {
    _state.value = _state.value.copy(fontSize = font)
    fontJob = viewModelScope.launch {
      fontJob?.cancel()
      delay(1000)
      sharedPreferences.edit().putInt(userPrefs.context.getString(R.string.font_key), font).apply()
      _state.value = _state.value.copy(
        fontSize = sharedPreferences
          .getInt(userPrefs.context.getString(R.string.font_key), font)
      )
    }
  }
}
