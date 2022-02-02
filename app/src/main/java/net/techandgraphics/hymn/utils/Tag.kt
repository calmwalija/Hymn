package net.techandgraphics.hymn.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

object Tag {
    const val TITLE = "hymn_title"
    const val NUMBER = "hymn_number"
    const val ADD_FAVORITE = "add_favorite"
    const val REMOVE_FAV = "remove_favorite"
    const val FONT = "font_size"
    const val CLEAR_FAV = "clear_favorites"
    const val KEYWORD = "search_keyword"
    const val DELETE = "delete_tag"
    const val SORT = "sort_by"
    const val SHARE = "share_hymn"
    const val LINK_OPEN = "link_open"

    const val ABOUT = "about_screen"
    const val OTHER = "other_screen"
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