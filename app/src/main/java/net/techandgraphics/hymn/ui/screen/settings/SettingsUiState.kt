package net.techandgraphics.hymn.ui.screen.settings

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.ui.screen.settings.export.SearchExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimeSpentExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimestampExport
import net.techandgraphics.hymn.ui.theme.AppTheme
import net.techandgraphics.hymn.ui.theme.FontManager

data class SettingsUiState(
  val fontSize: Int = LyricSizePreset.MEDIUM,
  val translation: Translation = Translation.EN,
  val dynamicColor: Boolean = true,
  val fontFamily: String = FontManager.Font.Default.font,
  val appTheme: AppTheme = AppTheme.SYSTEM,
  val hymnCount: Int = 0,
  val timeSpentExport: List<TimeSpentExport> = emptyList(),
  val timeStampExport: List<TimestampExport> = emptyList(),
  val searchExport: List<SearchExport> = emptyList(),
  val favExport: List<Int> = emptyList(),
)

object LyricSizePreset {
  const val SMALL = 1
  const val MEDIUM = 5
  const val LARGE = 10

  fun label(size: Int): String = when (nearest(size)) {
    SMALL -> "Small"
    LARGE -> "Large"
    else -> "Medium"
  }

  fun nearest(size: Int): Int = when {
    size <= 2 -> SMALL
    size <= 7 -> MEDIUM
    else -> LARGE
  }
}
