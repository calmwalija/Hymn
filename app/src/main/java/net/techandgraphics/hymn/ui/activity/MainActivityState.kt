package net.techandgraphics.hymn.ui.activity

import androidx.compose.ui.text.font.FontFamily

data class MainActivityState(
  val completed: Boolean = true,
  val dynamicColorEnabled: Boolean = false,
  val fontFamily: FontFamily = FontFamily.Default
)
