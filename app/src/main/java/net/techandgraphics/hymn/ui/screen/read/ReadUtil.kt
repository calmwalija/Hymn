package net.techandgraphics.hymn.ui.screen.read

enum class Direction {
  LEFT, RIGHT
}

data class HorizontalGesture(val old: Int, val new: Int)
