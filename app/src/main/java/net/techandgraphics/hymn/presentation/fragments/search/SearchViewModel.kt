package net.techandgraphics.hymn.presentation.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.map
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.asLyric
import net.techandgraphics.hymn.asLyricEntity
import net.techandgraphics.hymn.asSearch
import net.techandgraphics.hymn.asSearchEntity
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
  val repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  private var job: Job? = null
  private val searchInputEventChannel = Channel<SearchInputEvent>()

  val searchInput = searchInputEventChannel.receiveAsFlow()
  val searchQuery = MutableStateFlow("")
  val tag = repository.searchRepository.query.map {
    it.map { it.asSearch() }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  val lyric = searchQuery.flatMapLatest {
    job?.cancel()
    job = viewModelScope.launch {
      delay(3000)
      searchInputEventChannel.send(SearchInputEvent.TextChanged)
    }
    repository.lyricRepository.observeLyrics(it).map { it.map { it.asLyric() } }
  }

  fun insert(search: Search) =
    viewModelScope.launch { repository.searchRepository.upsert(listOf(search.asSearchEntity())) }

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.update(
        lyric.asLyricEntity().copy(favorite = !lyric.favorite)
      )
    }

  fun firebaseAnalyticsScreen() {
    Tag.screenView(firebaseAnalytics, Tag.SEARCH)
  }

  fun forTheService(lyric: Lyric) = viewModelScope.launch {
    repository.lyricRepository.update(
      lyric.asLyricEntity().copy(forTheService = true, ftsSuggestion = false)
    )
  }
}
