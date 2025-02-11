package net.techandgraphics.hymn.ui.screen.main

sealed class AnalyticEvent {
  class DiveInto(val number: Int) : AnalyticEvent()
  class Spotlight(val categoryId: Int) : AnalyticEvent()
  data class Keyboard(val keyboard: String) : AnalyticEvent()
}
