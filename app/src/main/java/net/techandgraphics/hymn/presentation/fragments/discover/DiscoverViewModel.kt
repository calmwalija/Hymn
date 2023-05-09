package net.techandgraphics.hymn.presentation.fragments.discover

import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import net.techandgraphics.hymn.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel
@Inject
constructor(
  repository: Repository,
  val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  val discover = repository.lyricRepository.observeCategories()
}
