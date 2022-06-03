package net.techandgraphics.hymn.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val repository: Repository,
    val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {


    private val lyricRepository = repository.lyricRepository
    private val searchRepository = repository.searchRepository
    private val otherRepository = repository.otherRepository

    private val channel = Channel<Callback>()
    val channelTask = channel.receiveAsFlow()

    private val _whenRead = MutableStateFlow(true)
    val whenRead: StateFlow<Boolean> = _whenRead.asStateFlow()

    fun onLoad() = viewModelScope.launch {
        _whenRead.value = repository.jsonParser.fromJson()
    }

    val searchQuery = MutableStateFlow("")

    private val flatMapLatest = searchQuery.flatMapLatest {
        lyricRepository.observeLyrics(it)
    }

    fun observeHymnLyrics() = flatMapLatest.asLiveData()
    fun observeCategories() = lyricRepository.observeCategories().asLiveData()
    fun observeTopPickCategories() = lyricRepository.observeTopPickCategories().asLiveData()
    fun observeRecentLyrics() = lyricRepository.observeRecentLyrics().asLiveData()
    fun observeSearch() = searchRepository.observeSearch().asLiveData()
    fun observeOther() = otherRepository.observeOther().asLiveData()
    fun findLyricById(id: Int) = lyricRepository.findLyricById(id).asLiveData()
    fun observeFavoriteLyrics() = lyricRepository.observeFavoriteLyrics().asLiveData()
    fun getLyricsById(lyric: Lyric) = lyricRepository.getLyricsById(lyric.number).asLiveData()
    fun getLyricsByCategory(lyric: Lyric) =
        lyricRepository.getLyricsByCategory(lyric.categoryId).asLiveData()

    fun insert(search: Search) = viewModelScope.launch {
        searchRepository.insert(listOf(search))
    }

    fun clearFavorite() = viewModelScope.launch { lyricRepository.clearFavorite() }

    fun clear() = viewModelScope.launch {
        lyricRepository.clear()
        searchRepository.clear()
        otherRepository.clear()
        channel.send(Callback.OnComplete)
    }


    fun update(lyric: Lyric) = viewModelScope.launch { lyricRepository.update(lyric) }

    fun delete(search: Search) = viewModelScope.launch {
        searchRepository.delete(search)
    }

    sealed class Callback {
        object OnComplete : Callback()
    }


}