package net.techandgraphics.hymn.ui.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.Repository
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.models.Search
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    init {
        viewModelScope.launch { repository.jsonLyricToDB() }
    }

    val searchQuery = MutableStateFlow("")

    private val flatMapLatest = searchQuery.flatMapLatest {
        repository.observeHymns(it)
    }

    fun observeHymnLyrics() = flatMapLatest.asLiveData()
    fun observeCategories() = repository.observeCategories.asLiveData()
    fun observeTopPickCategories() = repository.observeTopPickCategories.asLiveData()
    fun observeRecentLyrics() = repository.observeRecentLyrics.asLiveData()
    fun observeSearch() = repository.observeSearch.asLiveData()
    fun observeOther() = repository.observeOther.asLiveData()


    fun getLyricsById(lyric: Lyric) = repository.getLyricsById(lyric).asLiveData()
    fun getLyricsByCategory(lyric: Lyric) = repository.getLyricsByCategory(lyric).asLiveData()

    fun insert(search: Search) = viewModelScope.launch {
        repository.insert(listOf(search))
    }

    fun update(lyric: Lyric) =
        viewModelScope.launch {
            repository.update(
                lyric.copy(
                    timestamp = System.currentTimeMillis(),
                    topPickHit = lyric.topPickHit.plus(1)
                )
            )
        }

    fun delete(search: Search) = viewModelScope.launch {
        repository.delete(search)
    }

}