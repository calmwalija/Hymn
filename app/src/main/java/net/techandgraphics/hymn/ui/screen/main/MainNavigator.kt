package net.techandgraphics.hymn.ui.screen.main

sealed class MainNavigator {
  object NavigateToSearch : MainNavigator()
  object NavigateToCategory : MainNavigator()
}
