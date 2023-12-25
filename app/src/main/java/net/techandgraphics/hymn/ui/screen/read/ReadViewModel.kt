package net.techandgraphics.hymn.ui.screen.read

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.prefs.Prefs
import net.techandgraphics.hymn.domain.asModel
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.domain.toTimestampEntity
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val timestampRepo: TimestampRepository,
  private val prefs: Prefs
) : ViewModel() {

  private val _state = MutableStateFlow(ReadState())
  val state = _state.asStateFlow()

  private var fontJob: Job? = null
  private var translationJob: Job? = null

  private fun List<Lyric>.mapLyricKey(inverse: Boolean = true): List<LyricKey> {
    var currentPosition = 1
    return filter { if (inverse.not()) it.lang == prefs.lang else (it.lang != prefs.lang) }
      .map { lyric ->
        LyricKey(
          key = if (lyric.chorus == 0) (currentPosition++).toString() else prefs.context.getString(R.string.chorus),
          lyric = lyric
        )
      }
  }

  operator fun invoke(id: Int) = viewModelScope.launch {
    with(lyricRepo.queryByNumber(id)) {
      _state.value = _state.value.copy(
        lyricKeyInverse = mapLyricKey(true),
        lyricKey = mapLyricKey(false)
      )
      setLyricsData()
    }
  }

  private fun setLyricsData() = with(state.value) {
    viewModelScope.launch {
      _state.value = _state.value.copy(translationInverse = !state.value.translationInverse)
      val data = if (translationInverse) lyricKeyInverse else lyricKey
      _state.value = _state.value.copy(lyrics = data)
      translationJob?.cancel()
      delay(1000)
      read(data.first().lyric)
    }
  }

  private fun read(data: Lyric) = with(data) {
    viewModelScope.launch {
      lyricRepo.read(number, System.currentTimeMillis())
      timestampRepo.upsert(data.toTimestampEntity().asModel())
    }
  }

  fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
      invoke(lyric.number)
    }

  fun onEvent(event: ReadEvent) {
    when (event) {
      is ReadEvent.Click -> Unit
      is ReadEvent.Favorite -> favorite(event.data)
      is ReadEvent.FontSize -> fontSize(event.size)
      ReadEvent.TranslationInverse -> {
        setLyricsData()
      }
    }
  }

  private fun fontSize(font: Int) {
    _state.value = _state.value.copy(fontSize = font)
    fontJob = viewModelScope.launch {
      fontJob?.cancel()
      delay(1000)
      prefs.setFontSize(font)
      _state.value = _state.value.copy(
        fontSize = prefs.fontSize
      )
    }
  }
}
