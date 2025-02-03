package net.techandgraphics.hymn.ui.screen.settings

import androidx.compose.ui.text.font.FontFamily
import java.io.File

sealed interface SettingsChannelEvent {

  sealed class Export {
    data class Export(val file: File) : SettingsChannelEvent
  }

  sealed class Import {
    data class ProgressStatus(val total: Int, val currentProgress: Int)
    enum class Status { Wait, Invalid, Error, Success }
    data class Import(val status: Status) : SettingsChannelEvent
    data class Progress(val progressStatus: ProgressStatus) : SettingsChannelEvent
  }

  data class FontStyle(val fontFamily: FontFamily?) : SettingsChannelEvent
}
