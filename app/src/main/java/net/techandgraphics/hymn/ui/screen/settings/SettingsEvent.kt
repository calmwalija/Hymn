package net.techandgraphics.hymn.ui.screen.settings

import android.net.Uri
import androidx.compose.ui.text.font.FontFamily

sealed interface SettingsEvent {

  class Import(val uri: Uri) : SettingsEvent
  data class DynamicColor(val isEnabled: Boolean) : SettingsEvent
  data object Export : SettingsEvent

  sealed interface FontStyle : SettingsEvent {
    data class Selected(val fontFamily: FontFamily?, val fontName: String?) : FontStyle
    data class Apply(val fontFamily: FontFamily?) : FontStyle
    data object Default : FontStyle
    data object Choose : FontStyle
  }

  sealed interface Analytics : SettingsEvent {
    data object Feedback : Analytics
    data object Rating : Analytics
    data class ExportData(val timestamp: Long) : Analytics
    data class ThemeColor(val isEnabled: Boolean) : Analytics
    data class ImportData(val status: String, val fileName: String) : Analytics
    data class AppFontStyle(val fontFamily: String) : Analytics
  }
}
