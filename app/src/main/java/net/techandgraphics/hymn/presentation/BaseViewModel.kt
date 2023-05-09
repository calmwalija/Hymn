package net.techandgraphics.hymn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class BaseViewModel
@Inject
constructor(private val repository: Repository, val firebaseAnalytics: FirebaseAnalytics) :
  ViewModel() {

  @Inject lateinit var userPrefs: UserPrefs

  private val lyricRepository = repository.lyricRepository
  private val searchRepository = repository.searchRepository

  private val channel = Channel<Callback>()
  val channelTask = channel.receiveAsFlow()

  private val _whenRead = MutableStateFlow(true)
  val whenRead: StateFlow<Boolean> = _whenRead.asStateFlow()

  fun onLoad(init: Boolean) =
    viewModelScope.launch {
      userPrefs.setBuild(UserPrefs.BUILD)
      _whenRead.value = if (init.not()) false else repository.jsonParser.fromJson()
    }

  fun clearFavorite() = viewModelScope.launch { lyricRepository.clearFavorite() }

  fun clear() =
    viewModelScope.launch {
      lyricRepository.reset()
      searchRepository.clear()
      lyricRepository.clearFavorite()
      channel.send(Callback.OnComplete)
    }

  sealed class Callback {
    object OnComplete : Callback()
  }
}
