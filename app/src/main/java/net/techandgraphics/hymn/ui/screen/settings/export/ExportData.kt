package net.techandgraphics.hymn.ui.screen.settings.export

import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.local.entities.TimestampEntity

data class TimestampExport(val number: Int, val lang: String, val timestamp: Long) {
  fun toEntity() = TimestampEntity(number, lang, timestamp)
}

data class TimeSpentExport(val number: Int, val lang: String, val timeSpent: Long) {
  fun toEntity() = TimeSpentEntity(number, lang, timeSpent)
}

data class SearchExport(val query: String, val lang: String, val tag: String)

data class ExportData(
  val currentTimeMillis: Long = System.currentTimeMillis(),
  val favorites: List<Int>,
  val search: List<SearchExport>,
  val timeSpent: List<TimeSpentExport>,
  val timestamp: List<TimestampExport>,
  val hashable: String = ""
)
