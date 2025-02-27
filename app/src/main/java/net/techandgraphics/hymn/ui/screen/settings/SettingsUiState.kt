package net.techandgraphics.hymn.ui.screen.settings

import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.ui.screen.settings.export.SearchExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimeSpentExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimestampExport

data class SettingsUiState(
  val fontSize: Int = 2,
  val translation: Translation = Translation.EN,
  val dynamicColor: Boolean = true,
  val fontFamily: String? = null,
  val hymnCount: Int = 0,
  val timeSpentExport: List<TimeSpentExport> = emptyList(),
  val timeStampExport: List<TimestampExport> = emptyList(),
  val searchExport: List<SearchExport> = emptyList(),
  val favExport: List<Int> = emptyList(),
)
