package net.techandgraphics.hymn.ui.screen.app

import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route

val bottomNavigationList = listOf(
  BottomNavigationItem(
    route = Route.Home,
    selectedIcon = R.drawable.ic_filled_home,
    unSelectedIcon = R.drawable.ic_outline_home,
  ),
  BottomNavigationItem(
    route = Route.Search,
    selectedIcon = R.drawable.ic_filled_search,
    unSelectedIcon = R.drawable.ic_outline_search,
  ),
  BottomNavigationItem(
    route = Route.Mixed,
    selectedIcon = R.drawable.ic_filled_options,
    unSelectedIcon = R.drawable.ic_outline_options,
  )
)
