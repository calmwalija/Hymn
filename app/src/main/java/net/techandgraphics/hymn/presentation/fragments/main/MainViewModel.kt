package net.techandgraphics.hymn.presentation.fragments.main

import androidx.lifecycle.ViewModel
import androidx.paging.map
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import net.techandgraphics.hymn.asLyric
import net.techandgraphics.hymn.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
  repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  val lyric = repository.lyricRepository.observeLyrics().map { it.map { it.asLyric() } }
//  val essential = repository.essentialRepository.query.map { it.map { it.asEssential() } }
}
