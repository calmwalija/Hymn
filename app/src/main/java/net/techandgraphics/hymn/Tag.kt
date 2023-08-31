package net.techandgraphics.hymn

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Tag {
  const val TITLE = "hymn_title"
  const val NUMBER = "hymn_number"
  const val ADD_FAVORITE = "add_favorite"
  const val REMOVE_FAV = "remove_favorite"
  const val FONT = "font_size"
  const val THEME = "theme"
  const val BOOK_SWITCH = "book_switch"
  const val CLEAR_FAV = "clear_favorites"
  const val KEYWORD = "search_keyword"
  const val SEARCH_VIEW = "search_view"
  const val DISCOVER_VIEW = "discover_view"
  const val CLEAR_SEARCH_TAG = "clear_search_tag"
  const val APPEND_SEARCH_TAG = "append_search_tag"
  const val HYMN_BOOK = "hymn_book"
  const val HYMN_OF_THE_DAY = "hymn_of_the_day"
  const val HYMN_OF_THE_DAY_FAV = "hymn_of_the_day_fav"
  const val NOTIFICATION_HYMN_THRESHOLD = "notification_hymn_threshold"

  const val ABOUT = "about_screen"
  const val SEARCH = "search_screen"
  const val DISCOVER = "discover_screen"
  const val READ = "read_screen"
  const val FAVORITE = "favorite_screen"
  const val CATEGORY = "category_screen"

  fun screenView(firebaseAnalytics: FirebaseAnalytics, screen: String) {
    val bundle = Bundle()
    bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screen)
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
  }
}
