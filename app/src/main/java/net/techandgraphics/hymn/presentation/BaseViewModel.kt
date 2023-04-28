package net.techandgraphics.hymn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.entities.Lyric
import net.techandgraphics.hymn.data.local.entities.Search
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.presentation.fragments.search.SearchInputEvent
import javax.inject.Inject

@HiltViewModel
class BaseViewModel
@Inject
constructor(private val repository: Repository, val firebaseAnalytics: FirebaseAnalytics) :
  ViewModel() {

  @Inject lateinit var userPrefs: UserPrefs

  private val lyricRepository = repository.lyricRepository
  private val searchRepository = repository.searchRepository
  private val otherRepository = repository.otherRepository

  private val channel = Channel<Callback>()
  val channelTask = channel.receiveAsFlow()

  private val _whenRead = MutableStateFlow(true)
  val whenRead: StateFlow<Boolean> = _whenRead.asStateFlow()

  fun onLoad(init: Boolean) =
    viewModelScope.launch {
      userPrefs.setBuild(UserPrefs.BUILD)
      _whenRead.value = if (init.not()) false else repository.jsonParser.fromJson()
    }

  private var job: Job? = null
  private val searchInputEventChannel = Channel<SearchInputEvent>()
  val searchInput = searchInputEventChannel.receiveAsFlow()

  val searchQuery = MutableStateFlow("")

  private val flatMapLatest = searchQuery.flatMapLatest {
    job?.cancel()
    job = viewModelScope.launch {
      delay(3000)
      searchInputEventChannel.send(SearchInputEvent.TextChanged)
    }
    lyricRepository.observeLyrics(it)
  }

  fun observeHymnLyrics() = flatMapLatest.asLiveData()
  fun observeCategories() = lyricRepository.observeCategories()
  fun observeTopPickCategories() = lyricRepository.observeTopPickCategories().asLiveData()
  fun observeRecentLyrics() = lyricRepository.observeRecentLyrics().asLiveData()
  fun observeSearch() = searchRepository.observeSearch().asLiveData()
  fun observeOther() = otherRepository.observeOther().asLiveData()
  fun findLyricById(id: Int) = lyricRepository.findLyricById(id).asLiveData()
  fun observeFavoriteLyrics() = lyricRepository.observeFavoriteLyrics().asLiveData()
  fun getLyricsById(lyric: Lyric) = lyricRepository.getLyricsById(lyric.number).asLiveData()
  fun getLyricsByCategory(lyric: Lyric) =
    lyricRepository.getLyricsByCategory(lyric.categoryId).asLiveData()

  fun insert(search: Search) = viewModelScope.launch { searchRepository.insert(listOf(search)) }

  fun clearFavorite() = viewModelScope.launch { lyricRepository.clearFavorite() }

  fun clear() =
    viewModelScope.launch {
      lyricRepository.reset()
      searchRepository.clear()
      lyricRepository.clearFavorite()
      channel.send(Callback.OnComplete)
    }

  fun update(lyric: Lyric) = viewModelScope.launch { lyricRepository.update(lyric) }

  fun delete(search: Search) = viewModelScope.launch { searchRepository.delete(search) }

  sealed class Callback {
    object OnComplete : Callback()
  }

  val queryRandom = repository.lyricRepository.queryRandom
}
