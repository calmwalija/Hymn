package net.techandgraphics.hymn.ui.activity

import androidx.compose.ui.text.font.FontFamily
import net.techandgraphics.hymn.ui.theme.AppTheme

data class MainActivityState(
  val completed: Boolean = true,
  val dynamicColorEnabled: Boolean = false,
  val showStartupFailure: Boolean = false,
  val fontFamily: FontFamily = FontFamily.Default,
  val appTheme: AppTheme = AppTheme.SYSTEM,
)
