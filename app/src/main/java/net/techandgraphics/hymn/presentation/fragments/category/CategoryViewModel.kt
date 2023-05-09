package net.techandgraphics.hymn.presentation.fragments.category

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
class CategoryViewModel
@Inject
constructor(
  val repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.update(
        lyric.asLyricEntity().copy(favorite = !lyric.favorite)
      )
    }

  fun category(lyric: Lyric) =
    repository.lyricRepository.getLyricsByCategory(lyric.categoryId)
      .map { it.map { it.asLyric() } }

  fun firebaseAnalytics() {
    Tag.screenView(firebaseAnalytics, Tag.CATEGORY)
  }
}
