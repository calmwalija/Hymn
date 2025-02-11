package net.techandgraphics.hymn.ui.screen.preview

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.domain.toTimestampEntity
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val timestampRepo: TimestampRepository,
  private val timeSpentRepo: TimeSpentRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics,
  private val savedStateHandle: SavedStateHandle
) : ViewModel() {

  private val _state = MutableStateFlow(PreviewUiState())
  val state = _state.asStateFlow()
  private val currentTimeMillis = System.currentTimeMillis()
  private val maxTimeSpent: Long = 60_000.times(2)
  private var fontJob: Job? = null
  private var translationJob: Job? = null
  private val identifier = "identifier"

  private suspend fun getLang() = prefs.get(prefs.translationKey, Lang.EN.lowercase())

  private fun List<Lyric>.mapLyricKey(inverse: Boolean = true): List<PreviewLyricKey> {
    var currentPosition = 1
    return runBlocking {
      filter { if (inverse.not()) it.lang == getLang() else (it.lang != getLang()) }
        .map { lyric ->
          PreviewLyricKey(
            key = if (lyric.chorus == 0) (currentPosition++).toString() else prefs.context.getString(
              R.string.chorus
            ),
            lyric = lyric
          )
        }
    }
  }

  operator fun invoke(id: Int, setLyricsData: Boolean = true) = viewModelScope.launch {
    savedStateHandle[identifier] = id
    with(lyricRepo.queryByNumber(savedStateHandle.get<Int>(identifier) ?: 1)) {
      firebaseAnalytics(first())
      _state.value = _state.value.copy(
        previewLyricKeyInverse = mapLyricKey(true),
        previewLyricKey = mapLyricKey(false),
        fontSize = prefs.get(prefs.fontKey, 1.toString()).toInt()
      )
      if (setLyricsData.not()) return@launch
      setLyricsData()
    }
  }

  private fun setLyricsData() = with(state.value) {
    viewModelScope.launch {
      val data = if (translationInverse) previewLyricKeyInverse else previewLyricKey
      _state.update { it.copy(lyrics = data) }
      translationJob?.cancel()
      delay(1000)
      read(data.first().lyric)
      val currentHymn = state.value.lyrics.first().lyric.number
      val gotToPrevHymn = get(currentHymn.minus(1))?.number ?: -1
      val gotToNextHymn = get(currentHymn.plus(1))?.number ?: -1
      _state.update {
        it.copy(gotToPrevHymn = gotToPrevHymn, gotToNextHymn = gotToNextHymn)
      }
    }
  }

  private suspend fun get(theNumber: Int) = lyricRepo.queryByNumber(theNumber).firstOrNull()

  private fun read(data: Lyric) = with(data) {
    viewModelScope.launch {
      val currentTimeMillis = System.currentTimeMillis()
      lyricRepo.read(number, currentTimeMillis, null)
      timestampRepo.upsert(listOf(data.toTimestampEntity().copy(timestamp = currentTimeMillis)))
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
        invoke(lyric.number, false)
      }
    }

  fun onEvent(event: PreviewUiEvent) {
    when (event) {
      is PreviewUiEvent.Click -> Unit
      is PreviewUiEvent.Favorite -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.READ_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is PreviewUiEvent.FontSize -> {
        analytics.tagEvent(Tag.FONT_SIZE, bundleOf(Pair(Tag.READ_SCREEN, event.size.toString())))
        fontSize(event.size)
      }

      PreviewUiEvent.TranslationInverse -> {
        analytics.tagEvent(
          Tag.TRANSLATION_INVERSE,
          bundleOf(Pair(Tag.READ_SCREEN, !state.value.translationInverse))
        )
        _state.update { it.copy(translationInverse = !state.value.translationInverse) }
        setLyricsData()
      }

      is PreviewUiEvent.Invoke -> {
        event.theNumber.takeIf { it > 0 }?.let { invoke(event.theNumber) }
      }
    }
  }

  private fun firebaseAnalytics(lyric: Lyric) = with(analytics) {
    tagScreen(Tag.READ_SCREEN)
    tagEvent(Tag.HYMN_TITLE, bundleOf(Pair(Tag.HYMN_TITLE, lyric.title)))
    tagEvent(Tag.HYMN_NUMBER, bundleOf(Pair(Tag.HYMN_NUMBER, lyric.number)))
  }

  private fun fontSize(font: Int) = viewModelScope.launch {
    _state.value = _state.value.copy(fontSize = font)
    fontJob?.cancel()
    fontJob = viewModelScope.launch {
      delay(1000)
      prefs.put(prefs.fontKey, font.toString())
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
