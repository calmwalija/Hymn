package net.techandgraphics.hymn.ui.screen

import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

object Event {

  fun category(event: CategoryEvent): String {
    return when (event) {
      is CategoryEvent.Click ->
        String.format("%s/%d", Route.Categorisation.title, event.categoryId)
    }
  }

  fun read(event: ReadEvent): String {
    return when (event) {
      is ReadEvent.Click ->
        String.format("%s/%d", Route.Read.title, event.number)
    }
  }
}
