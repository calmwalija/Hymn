package net.techandgraphics.hymn.presentation.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.asLyric
import net.techandgraphics.hymn.asLyricEntity
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.timeInMillisMonth
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel
@Inject
constructor(
  val repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics,
  private val userPrefs: UserPrefs
) : ViewModel() {

  val ofTheDay = MutableStateFlow<List<Lyric>>(emptyList())
  val featuredHymn = MutableStateFlow<List<Discover>>(emptyList())
  val theHymn = MutableStateFlow(emptyList<Lyric>())
  val forTheService = repository.lyricRepository.forTheService.map { it.map { it.asLyric() } }
  val forTheServiceData = repository.lyricRepository.queryLyrics.map { it.map { it.asLyric() } }
  val onBoarding = userPrefs.getOnBoarding
  val donatePeriod = userPrefs.getDonatePeriod
  private val justAddedFlow = repository.lyricRepository.justAdded.map { it.map { it.asLyric() } }
  private val theHymnFlow = repository.lyricRepository.theHymn.map { it.map { it.asLyric() } }
  private val data = mutableListOf(emptyList<Lyric>())

  fun donatePeriod(month: Int = 1) = viewModelScope.launch {
    userPrefs.donatePeriod(timeInMillisMonth(month))
  }

  fun newHymn(id: Long) =
    repository.lyricRepository.getLyricsByIdRangeLang(id).map { it.map { it.asLyric() } }

  private fun ofTheDay() {
    userPrefs.getMills.combine(userPrefs.getOfTheDay) { mills, number ->
      val maxValue = repository.lyricRepository.lastInsertedHymn() ?: return@combine
      val random = Random.nextInt(maxValue)
      if (mills == null || Utils.currentMillsDiff(mills)) {
        userPrefs.mills(System.currentTimeMillis())
        userPrefs.ofTheDay(random)
      }
      if (number == null) return@combine
      repository.lyricRepository.getLyricsById(number).collect {
        if (it.isEmpty().not())
          ofTheDay.value = listOf(it.map { it.asLyric() }.first())
      }
    }.launchIn(viewModelScope)
  }

  private fun featuredCategory() {
    userPrefs.getOfTheDay.onEach {
      val maxValue = repository.lyricRepository.lastInsertedHymn() ?: return@onEach
      val size = repository.lyricRepository.categoryCount().size
      if (it == null) return@onEach
//      val limit = Utils.getThreshold(maxValue, size, it)
      repository.lyricRepository.featuredHymn(Random.nextInt(Utils.FEATURE_LIMIT))
        .collect { featuredHymn.value = it }
    }.launchIn(viewModelScope)
  }

  private suspend fun List<Lyric>.resetJustAdded() {
    val aWeek = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -8) }.timeInMillis
    repository.lyricRepository.upsert(
      map {
        it.asLyricEntity().copy(justAdded = it.millsAdded < aWeek)
      }
    )
  }

  private fun deleteBecauseHeLives() = with(repository.lyricRepository) {
    getLyricsById(385).onEach {
      if (it.isEmpty().not())
        deleteBecauseHeLives()
    }.launchIn(viewModelScope)
  }

  init {
    ofTheDay()
    featuredCategory()
    forTheServiceSuggestion()
    deleteBecauseHeLives()
    combine(theHymnFlow, justAddedFlow) { hymn, justAdded ->
      data.clear()
      justAdded.resetJustAdded()
      data.addAll(listOf(justAdded.plus(hymn)))
      theHymn.value = data.flatten().distinctBy { it.lyricId }
    }.launchIn(viewModelScope)
  }

  fun forTheService(lyric: Lyric) = viewModelScope.launch {
    repository.lyricRepository.forTheServiceUpdate(
      lyric.asLyricEntity().copy(forTheService = lyric.forTheService.not(), ftsSuggestion = false)
    )
  }

  private fun LyricEntity.toSuggestion() = copy(forTheService = true, ftsSuggestion = true)
  private fun forTheServiceSuggestion() {
    forTheService.onEach {
      if (it.isEmpty())
        with(repository.lyricRepository) {
          queryRandom()?.toSuggestion()?.let { forTheServiceUpdate(it) }
        }
    }.launchIn(viewModelScope)
  }

  fun onBoarding() = viewModelScope.launch { userPrefs.onBoarding(true) }

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.update(
        lyric.asLyricEntity().copy(favorite = !lyric.favorite)
      )
    }
}
