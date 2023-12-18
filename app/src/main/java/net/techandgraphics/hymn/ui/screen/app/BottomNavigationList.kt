package net.techandgraphics.hymn.ui.screen.app

import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route

val bottomNavigationList = listOf(
  BottomNavigationItem(
    title = Route.Home.title,
    selectedIcon = R.drawable.ic_filled_home,
    unSelectedIcon = R.drawable.ic_outline_home,
  ),
  BottomNavigationItem(
    title = Route.Search.title,
    selectedIcon = R.drawable.ic_search,
    unSelectedIcon = R.drawable.ic_search,
  ),
  BottomNavigationItem(
    title = Route.Miscellaneous.title,
    selectedIcon = R.drawable.ic_filled_miscellaneous,
    unSelectedIcon = R.drawable.ic_outline_miscellaneous,
  )
)
