package net.techandgraphics.hymn.presentation.fragments.search

sealed interface SearchInputEvent {
  object TextChanged : SearchInputEvent
}