package net.techandgraphics.hymn.ui.screen.main

sealed class MainNavigator {
  data object NavigateToSearch : MainNavigator()
  data object NavigateToCategory : MainNavigator()
}
