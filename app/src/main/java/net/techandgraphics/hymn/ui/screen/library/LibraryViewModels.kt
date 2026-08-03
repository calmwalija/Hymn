package net.techandgraphics.hymn.ui.screen.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
  lyricRepo: LyricRepository,
) : ViewModel() {
  val favorites = lyricRepo.favorites()
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@HiltViewModel
class HistoryViewModel @Inject constructor(
  lyricRepo: LyricRepository,
) : ViewModel() {
  val history = lyricRepo.recentHistory(50)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<Lyric>())
}
