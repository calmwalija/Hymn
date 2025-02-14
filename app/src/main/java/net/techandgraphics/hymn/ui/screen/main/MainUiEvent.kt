package net.techandgraphics.hymn.ui.screen.main

import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric

sealed interface MainUiEvent {

  class ChangeTranslation(val lang: String) : MainUiEvent

  data class GotoPreview(val lyric: Lyric) : MainUiEvent
  data class GotoCategory(val category: Category) : MainUiEvent
  data object FeaturedCategories : MainUiEvent

  sealed class MenuItem {
    data object Settings : MainUiEvent
    data object ApostlesCreed : MainUiEvent
    data object LordsPrayer : MainUiEvent
    data object Favorites : MainUiEvent
  }

  sealed class LyricEvent {
    class LyricSearch(val searchQuery: String) : MainUiEvent
    class QueryTag(val searchQuery: String) : MainUiEvent
    data object InsertSearchTag : MainUiEvent
    data object ClearSearchQuery : MainUiEvent
  }

  sealed interface AnalyticEvent : MainUiEvent {

    data class KeyboardType(val keyboardType: String) : AnalyticEvent
    data class SearchEmptyState(val keyword: String) : AnalyticEvent
    data object ShowTranslationDialog : AnalyticEvent
    data object ShowMenuDialog : AnalyticEvent
    data object ShowFavoriteDialog : AnalyticEvent
    data object ShowApostlesCreedDialog : AnalyticEvent
    data object ShowLordsPrayerDialog : AnalyticEvent
    data object GotoSettingScreen : AnalyticEvent
    data object ShowFeaturedCategoriesDialog : AnalyticEvent
    data class GotoPreviewFromFavorite(val lyric: Lyric) : AnalyticEvent
    data class GotoPreviewFromUniquelyCrafted(val lyric: Lyric) : AnalyticEvent
    data class GotoPreviewFromDiveInto(val theNumber: Int) : AnalyticEvent
    data class GotoTheCategory(val category: Category) : AnalyticEvent
    data class GotoPreviewFromSearch(val lyric: Lyric) : AnalyticEvent
  }
}
