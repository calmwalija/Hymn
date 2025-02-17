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
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.domain.toTimestampEntity
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.combined
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
  private val identifier = "identifier"
  private val defaultFontSize = "1"
  private var fontJob: Job? = null

  private suspend fun getTranslation() = prefs.get(prefs.translationKey, Translation.EN.lowercase())
  private suspend fun get(theNumber: Int) = lyricRepo.queryByNumber(theNumber).firstOrNull()
  private fun onInvoke(theNumber: Int) = theNumber.takeIf { it > 0 }?.let { invoke(theNumber) }

  private fun List<Lyric>.toIndex(): List<LyricWithKey> {
    var lyricIndex = 1
    return map { lyric ->
      LyricWithKey(
        index = if (lyric.chorus == 0) (lyricIndex++).toString() else prefs.context.getString(
          R.string.chorus
        ),
        lyric = lyric
      )
    }
  }

  private fun currentLyric(translation: String) {
    val lyricsWithIndex = state.value.lyrics.filter { it.lang == translation }.toIndex()
    _state.update { it.copy(lyricsWithIndex = lyricsWithIndex) }
    _state.update { it.copy(currentLyric = lyricsWithIndex.first().lyric) }
    _state.update { it.copy(currentTranslation = translation) }
  }

  operator fun invoke(theHymnNumber: Int) = viewModelScope.launch {
    savedStateHandle[identifier] = theHymnNumber
    with(lyricRepo.queryByNumber(savedStateHandle.get<Int>(identifier) ?: 1)) {
      _state.update { it.copy(lyrics = this) }
      _state.update { it.copy(translations = map { lyric -> lyric.lang }.distinct()) }
      _state.update { it.copy(theHymnNumber = theHymnNumber) }
      _state.update { it.copy(defaultTranslation = getTranslation()) }
      _state.update { it.copy(categoryId = first { lyric -> lyric.lang == getTranslation() }.categoryId) }
      _state.update { it.copy(fontSize = prefs.get(prefs.fontKey, defaultFontSize).toInt()) }
      currentLyric(state.value.defaultTranslation)
      onLogLyricInfo(state.value.currentLyric!!)
      state.value.currentLyric?.let { onLogFirebaseAnalytics(it) }
      canGoBackAndForth()
    }
  }

  private fun canGoBackAndForth() = viewModelScope.launch {
    val theHymnNumber = state.value.theHymnNumber
    val gotToPrevHymn = get(theHymnNumber.minus(1))?.number ?: -1
    val gotToNextHymn = get(theHymnNumber.plus(1))?.number ?: -1
    _state.update { it.copy(gotToPrevHymn = gotToPrevHymn, gotToNextHymn = gotToNextHymn) }
  }

  private fun onLogLyricInfo(lyric: Lyric) = with(lyric) {
    viewModelScope.launch {
      val currentTimeMillis = System.currentTimeMillis()
      val timestampEntity = lyric.toTimestampEntity().copy(timestamp = currentTimeMillis)
      lyricRepo.read(number, currentTimeMillis, state.value.defaultTranslation)
      timestampRepo.upsert(listOf(timestampEntity))
    }
  }

  private fun onFavoriteHymn(lyric: Lyric) = viewModelScope.launch {
    with(lyric.copy(favorite = !lyric.favorite)) {
      analytics.tagEvent(
        if (!lyric.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
        bundleOf(Pair(Tag.ADD_FAVORITE, lyric.title))
      )
      lyricRepo.favorite(favorite, number)
      val lyrics = state.value.lyrics.map {
        it.copy(favorite = if (it.lang == state.value.defaultTranslation) favorite else lyric.favorite)
      }
      val currentLyric = state.value.currentLyric!!.copy(favorite = favorite)
      _state.update { it.copy(lyrics = lyrics) }
      _state.update { it.copy(currentLyric = currentLyric) }
    }
  }

  private fun onChangeTranslation() = viewModelScope.launch {
    val currentTranslation = state.value.currentTranslation
    val translation = state.value.translations.first { it != currentTranslation }
    analytics.tagEvent(Tag.CHANGE_TRANSLATION, bundleOf(Pair(Tag.CHANGE_TRANSLATION, translation)))
    _state.update { it.copy(currentTranslation = translation) }
    currentLyric(state.value.currentTranslation)
  }

  fun onEvent(event: PreviewUiEvent) {
    when (event) {
      is PreviewUiEvent.Favorite -> onFavoriteHymn(event.data)
      is PreviewUiEvent.FontSize -> onChangeFontSize(event.size)
      is PreviewUiEvent.Invoke -> onInvoke(event.theNumber)
      PreviewUiEvent.ChangeTranslation -> onChangeTranslation()
      PreviewUiEvent.Analytics.FontDialog -> logFontDialog()
      PreviewUiEvent.Analytics.SwipeToRight -> logSwipeToRight()
      PreviewUiEvent.Analytics.SwipeToLeft -> logSwipeToLeft()
      PreviewUiEvent.Analytics.GotoTheCategory -> logGotoTheCategory()
      is PreviewUiEvent.Analytics.GotoNextHymn -> logGoToNextHymn(event.theNumber)
      is PreviewUiEvent.Analytics.GotoPreviousHymn -> logGoToPreviousHymn(event.theNumber)

      else -> Unit
    }
  }

  private fun logSwipeToLeft() {
    analytics.combined(
      name = Tag.SWIPE_TO_LEFT,
      Pair(Tag.HYMN_NUMBER, state.value.theHymnNumber),
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
    )
  }

  private fun logSwipeToRight() {
    analytics.combined(
      name = Tag.SWIPE_TO_RIGHT,
      Pair(Tag.HYMN_NUMBER, state.value.theHymnNumber),
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
    )
  }

  private fun logGotoTheCategory() =
    analytics.combined(
      name = Tag.GO_TO_THE_CATEGORY,
      Pair(Tag.HYMN_NUMBER, state.value.theHymnNumber),
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
    )

  private fun logGoToPreviousHymn(theNumber: Int) {
    analytics.combined(
      name = Tag.GO_TO_PREVIOUS_HYMN,
      Pair(Tag.HYMN_NUMBER, theNumber),
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
    )
  }

  private fun logGoToNextHymn(theNumber: Int) =
    analytics.combined(
      name = Tag.GO_TO_NEXT_HYMN,
      Pair(Tag.HYMN_NUMBER, theNumber),
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
    )

  private fun logFontDialog() =
    analytics.combined(
      Tag.FONT_SIZE_DIALOG,
      Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
      Pair(Tag.FONT_SIZE_DIALOG_STATE, "Open"),
    )

  private fun onLogFirebaseAnalytics(lyric: Lyric) = with(analytics) {
    tagScreen(Tag.PREVIEW_SCREEN)
    combined(
      name = Tag.HYMN_TITLE,
      Pair(Tag.HYMN_TITLE, lyric.title),
      Pair(Tag.HYMN_NUMBER, lyric.number),
    )
  }

  private fun onChangeFontSize(fontSize: Int) {
    _state.update { it.copy(fontSize = fontSize) }
    fontJob?.cancel()
    fontJob = viewModelScope.launch {
      delay(500)
      prefs.put(prefs.fontKey, "$fontSize")
      analytics.combined(
        name = Tag.FONT_SIZE,
        Pair(Tag.FONT_SIZE, fontSize),
        Pair(Tag.TRANSLATION_DEFAULT, state.value.defaultTranslation),
      )
    }
  }

  override fun onCleared() {
    val timeSpentMills = System.currentTimeMillis() - currentTimeMillis
    if (timeSpentMills < 1) return
    val currentLyric = state.value.currentLyric!!
    val timeSpent = TimeSpentEntity(
      number = currentLyric.number,
      timeSpent = if (timeSpentMills > maxTimeSpent) maxTimeSpent else timeSpentMills,
      lang = currentLyric.lang
    )
    runBlocking { withContext(Dispatchers.IO) { timeSpentRepo.upsert(listOf(timeSpent)) } }
  }
}
