package net.techandgraphics.hymn.ui.screen.read

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.prefs.AppPrefs
import net.techandgraphics.hymn.data.prefs.SharedPrefs
import net.techandgraphics.hymn.data.prefs.getLang
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.domain.toTimestampEntity
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.fontSize
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val timestampRepo: TimestampRepository,
  private val timeSpentRepo: TimeSpentRepository,
  private val prefs: SharedPrefs,
  private val appPrefs: AppPrefs,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(ReadState())
  val state = _state.asStateFlow()
  private val currentTimeMillis = System.currentTimeMillis()
  private val maxTimeSpent: Long = 60_000
  private var fontJob: Job? = null
  private var translationJob: Job? = null

  private fun List<Lyric>.mapLyricKey(inverse: Boolean = true): List<LyricKey> {
    var currentPosition = 1
    return filter { if (inverse.not()) it.lang == prefs.getLang() else (it.lang != prefs.getLang()) }
      .map { lyric ->
        LyricKey(
          key = if (lyric.chorus == 0) (currentPosition++).toString() else prefs.context.getString(
            R.string.chorus
          ),
          lyric = lyric
        )
      }
  }

  operator fun invoke(id: Int, setLyricsData: Boolean = true) = viewModelScope.launch {
    with(lyricRepo.queryByNumber(id)) {
      firebaseAnalytics(first())
      _state.value = _state.value.copy(
        lyricKeyInverse = mapLyricKey(true),
        lyricKey = mapLyricKey(false),
        fontSize = appPrefs.fontSize()
      )
      if (setLyricsData.not()) return@launch
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
      timestampRepo.upsert(listOf(data.toTimestampEntity()))
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
        invoke(lyric.number, false)
      }
    }

  fun onEvent(event: ReadEvent) {
    when (event) {
      is ReadEvent.Click -> Unit
      is ReadEvent.Favorite -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.READ_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is ReadEvent.FontSize -> {
        analytics.tagEvent(Tag.FONT_SIZE, bundleOf(Pair(Tag.READ_SCREEN, event.size.toString())))
        fontSize(event.size)
      }

      ReadEvent.TranslationInverse -> {
        analytics.tagEvent(
          Tag.TRANSLATION_INVERSE,
          bundleOf(Pair(Tag.READ_SCREEN, !state.value.translationInverse))
        )
        setLyricsData()
      }
    }
  }

  private fun firebaseAnalytics(lyric: Lyric) = with(analytics) {
    tagScreen(Tag.READ_SCREEN)
    tagEvent(Tag.HYMN_TITLE, bundleOf(Pair(Tag.HYMN_TITLE, lyric.title)))
    tagEvent(Tag.HYMN_NUMBER, bundleOf(Pair(Tag.HYMN_NUMBER, lyric.number)))
  }

  private fun fontSize(font: Int) {
    _state.value = _state.value.copy(fontSize = font)
    fontJob = viewModelScope.launch {
      fontJob?.cancel()
      delay(1000)
      appPrefs.setPrefs(appPrefs.fontKey, font.toString())
    }
  }

  override fun onCleared() {
    val timeSpentMills = System.currentTimeMillis() - currentTimeMillis
    if (timeSpentMills < 1) return
    val currentLyric = state.value.lyrics.first().lyric
    val timeSpent = TimeSpentEntity(
      number = currentLyric.number,
      timeSpent = if (timeSpentMills > maxTimeSpent) maxTimeSpent else timeSpentMills,
      lang = currentLyric.lang
    )
    runBlocking { withContext(Dispatchers.IO) { timeSpentRepo.upsert(listOf(timeSpent)) } }
  }
}
