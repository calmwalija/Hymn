package net.techandgraphics.hymn.presentation.fragments.read

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.asLyric
import net.techandgraphics.hymn.asLyricEntity
import net.techandgraphics.hymn.data.repository.Repository
import net.techandgraphics.hymn.domain.model.Lyric
import javax.inject.Inject

@HiltViewModel
class ReadViewModel
@Inject
constructor(
  val repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  val state = MutableStateFlow(ReadState())

  fun lyric(lyric: Lyric) = repository.lyricRepository
    .getLyricsById(lyric.number).map {
      it.map {
        it.asLyric()
      }
    }

  fun update(lyric: Lyric) =
    viewModelScope.launch {
      repository.lyricRepository.getLyricsById(lyric.number).first().filter { it.timestamp != 0L }
        .let {
          val timestamp =
            if (it.isEmpty().not()) it.first().timestamp else System.currentTimeMillis()
          repository.lyricRepository.update(lyric.asLyricEntity().copy(timestamp = timestamp))
        }
    }

  fun firebaseAnalytics(lyric: Lyric) {
    firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.TITLE, lyric.title)))
    firebaseAnalytics.logEvent(Tag.NUMBER, bundleOf(Pair(Tag.NUMBER, lyric.number)))
    Tag.screenView(firebaseAnalytics, Tag.READ)
  }
}
