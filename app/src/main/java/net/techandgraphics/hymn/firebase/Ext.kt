package net.techandgraphics.hymn.firebase

import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

fun FirebaseAnalytics.tagScreen(screen: String) {
  val bundle = bundleOf(Pair(FirebaseAnalytics.Param.SCREEN_NAME, screen))
  logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
}

fun FirebaseAnalytics.tagEvent(name: String, bundle: Bundle) {
  logEvent(name, bundle)
}

fun FirebaseAnalytics.tagEvent(name: String, pair: Pair<String, Any>) {
  logEvent(name, bundleOf(pair))
}

fun FirebaseAnalytics.combined(name: String, vararg thePairs: Pair<String, Any>) {
  logEvent(name, bundleOf(*thePairs))
}
