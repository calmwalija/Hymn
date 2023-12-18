package net.techandgraphics.hymn.ui.screen.app

import androidx.annotation.DrawableRes

data class BottomNavigationItem(
  val title: String,
  @DrawableRes val selectedIcon: Int,
  @DrawableRes val unSelectedIcon: Int,
)
