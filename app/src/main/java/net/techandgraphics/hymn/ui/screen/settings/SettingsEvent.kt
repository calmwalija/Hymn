package net.techandgraphics.hymn.ui.screen.settings

import android.net.Uri
import androidx.compose.ui.text.font.FontFamily
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.ui.theme.AppTheme

sealed interface SettingsEvent {

  class Import(val uri: Uri) : SettingsEvent
  data class DynamicColor(val isEnabled: Boolean) : SettingsEvent
  data object Export : SettingsEvent
  data object ResetListeningStats : SettingsEvent
  data class ThemeMode(val theme: AppTheme) : SettingsEvent
  data class LyricSize(val size: Int) : SettingsEvent
  data class ChangeTranslation(val translation: Translation) : SettingsEvent

  sealed interface FontStyle : SettingsEvent {
    data class Selected(val fontName: String) : FontStyle
    data class Apply(val fontFamily: FontFamily?) : FontStyle
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
