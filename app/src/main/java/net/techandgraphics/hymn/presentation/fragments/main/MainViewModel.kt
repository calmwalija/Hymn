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
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.timeInMillisMonth
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
  val theHymn = repository.lyricRepository.theHymn.map { it.map { it.asLyric() } }
  val onBoarding = userPrefs.getOnBoarding
  val donatePeriod = userPrefs.getDonatePeriod

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

  init {
    ofTheDay()
    featuredCategory()
  }

  fun onBoarding() = viewModelScope.launch { userPrefs.onBoarding(true) }

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.update(
        lyric.asLyricEntity().copy(favorite = !lyric.favorite)
      )
    }
}
