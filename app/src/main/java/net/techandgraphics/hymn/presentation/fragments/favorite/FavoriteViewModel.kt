package net.techandgraphics.hymn.presentation.fragments.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.asLyric
import net.techandgraphics.hymn.asLyricEntity
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
  private val repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  val favorite = repository.lyricRepository.favorite.map { it.map { it.asLyric() } }

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.update(
        lyric.copy(favorite = !lyric.favorite).asLyricEntity()
      )
    }

  fun firebaseAnalytics() {
    Tag.screenView(firebaseAnalytics, Tag.FAVORITE)
  }
}
