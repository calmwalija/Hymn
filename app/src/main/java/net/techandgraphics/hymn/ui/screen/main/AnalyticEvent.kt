package net.techandgraphics.hymn.ui.screen.main

sealed class AnalyticEvent {
  object GotoSearch : AnalyticEvent()
  object GotoCategory : AnalyticEvent()
  class DiveInto(val number: Int) : AnalyticEvent()
  class Spotlight(val categoryId: Int) : AnalyticEvent()
}
