package net.techandgraphics.hymn.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.data.local.Database
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.data.prefs.UserPrefs
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MainViewModel @Inject constructor(
  private val database: Database,
  private val version: String,
  private val userPrefs: UserPrefs
) : ViewModel() {

  private val theHymn = database.lyricDao.theHymn(version)
  private val _state = MutableStateFlow(MainState())

  val state = _state.asStateFlow()

  init {
    ofTheDay()
    viewModelScope.launch {
      _state.value = _state.value.copy(featured = database.categoryDao.featured(version))
      theHymn.onEach { _state.value = _state.value.copy(theHymn = it) }.launchIn(this)
    }
  }

  private fun ofTheDay() {
    userPrefs.getMills.combine(userPrefs.getOfTheDay) { mills, number ->
      val maxValue = database.lyricDao.lastInsertedId(version) ?: return@combine
      val random = Random.nextInt(maxValue)
      if (mills == null || Utils.currentMillsDiff(mills)) {
        userPrefs.mills(System.currentTimeMillis())
        userPrefs.ofTheDay(random)
      }
      if (number == null) return@combine
      database.lyricDao.queryById(2, version).collect {
        _state.value = _state.value.copy(ofTheDay = it)
      }
    }.launchIn(viewModelScope)
  }

  fun favorite(lyric: LyricEntity) =
    viewModelScope.launch {
      database.lyricDao.upsert(listOf(lyric.copy(favorite = !lyric.favorite)))
    }

  fun onEvent(event: MainEvent) {
    when (event) {
      is MainEvent.Favorite -> favorite(event.data)
    }
  }
}
