package net.techandgraphics.hymn.ui.screen.settings

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.settings.export.SearchExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimeSpentExport
import net.techandgraphics.hymn.ui.screen.settings.export.TimestampExport

data class SettingsUiState(
  val favorites: List<Lyric> = emptyList(),
  val fontSize: Int = 2,
  val lang: Lang = Lang.EN,
  val dynamicColor: Boolean = true,
  val fontFamily: String? = null,
  val timeSpentExport: List<TimeSpentExport> = emptyList(),
  val timeStampExport: List<TimestampExport> = emptyList(),
  val searchExport: List<SearchExport> = emptyList(),
  val favExport: List<Int> = emptyList(),
)
