package net.techandgraphics.hymn.ui.screen.app

import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route

val bottomNavigationList = listOf(
  BottomNavigationItem(
    title = "Home",
    route = Route.Home,
    selectedIcon = R.drawable.ic_filled_home,
    unSelectedIcon = R.drawable.ic_outline_home,
  ),
  BottomNavigationItem(
    title = "Search",
    route = Route.Searching(),
    selectedIcon = R.drawable.ic_filled_search,
    unSelectedIcon = R.drawable.ic_outline_search,
  ),
  BottomNavigationItem(
    title = "Mixed",
    route = Route.Mixed,
    selectedIcon = R.drawable.ic_filled_options,
    unSelectedIcon = R.drawable.ic_outline_options,
  )
)
