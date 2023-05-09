package net.techandgraphics.hymn.presentation.fragments.essential

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.domain.model.Essential
import javax.inject.Inject

@HiltViewModel
class EssentialViewModel
@Inject
constructor(
  private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

  fun firebaseAnalytics(essential: Essential) {
    firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.TITLE, essential.groupName)))
    firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.LANG, essential.lang)))
    firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.CONTENT, essential.content)))
    firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.RES_ID, essential.resourceId)))
    Tag.screenView(firebaseAnalytics, Tag.ESSENTIAL)
  }
}
