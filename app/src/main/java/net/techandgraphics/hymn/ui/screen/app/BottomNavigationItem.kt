package net.techandgraphics.hymn.ui.screen.app

import androidx.annotation.DrawableRes
import net.techandgraphics.hymn.ui.Route

data class BottomNavigationItem(
  val route: Route,
  val title: String,
  @DrawableRes val selectedIcon: Int,
  @DrawableRes val unSelectedIcon: Int,
)
