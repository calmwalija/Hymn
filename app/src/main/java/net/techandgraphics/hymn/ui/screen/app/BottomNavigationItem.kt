package net.techandgraphics.hymn.ui.screen.app

import androidx.annotation.DrawableRes
import net.techandgraphics.hymn.ui.Route

data class BottomNavigationItem(
  val route: Route,
  @DrawableRes val selectedIcon: Int,
  @DrawableRes val unSelectedIcon: Int,
)
