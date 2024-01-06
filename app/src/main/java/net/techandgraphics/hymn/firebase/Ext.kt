package net.techandgraphics.hymn.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

fun FirebaseAnalytics.tagScreen(screen: String) {
  val bundle = Bundle().apply {
    putString(FirebaseAnalytics.Param.SCREEN_NAME, screen)
  }
  logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
}

fun FirebaseAnalytics.tagEvent(name: String, bundle: Bundle) {
  logEvent(name, bundle)
}
