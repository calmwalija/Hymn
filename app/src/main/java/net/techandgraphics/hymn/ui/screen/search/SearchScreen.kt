package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import net.techandgraphics.hymn.ui.screen.component.ToggleSwitch
import net.techandgraphics.hymn.ui.screen.component.ToggleSwitchItem
import net.techandgraphics.hymn.ui.screen.search.category.CategoryScreen
import net.techandgraphics.hymn.ui.screen.search.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.search.lyric.LyricScreen
import net.techandgraphics.hymn.ui.screen.search.lyric.LyricUiEvent
import net.techandgraphics.hymn.ui.screen.search.lyric.LyricViewModel

open class ToggleSwitchSearchItem : ToggleSwitchItem {
  data object Category : ToggleSwitchSearchItem()
  data object Hymn : ToggleSwitchSearchItem()
}

@Composable
fun SearchScreen(
  activeTab: Int,
  lyricViewModel: LyricViewModel,
  categoryViewModel: CategoryViewModel,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    var tabSelected by remember { mutableIntStateOf(activeTab) }
    ToggleSwitch(
      title = "Quick Search",
      toggleSwitchItems = ToggleSwitchSearchItem::class.nestedClasses.sortedByDescending { it.simpleName },
      tabSelected = tabSelected
    ) { tabSelected = it }
    if (tabSelected == 0) Tab1(viewModel = lyricViewModel) else Tab2(viewModel = categoryViewModel)
  }
}

@Composable
fun Tab2(viewModel: CategoryViewModel) {
  val state = viewModel.state.collectAsState().value
  CategoryScreen(state) { event ->
    when (event) {
//      is CategoryUiEvent.Click ->
//        navController.navigate(Route.Categorisation(event.id)) {
//          launchSingleTop = true
//        }

      else -> viewModel.onEvent(event)
    }
  }
}

@Composable
fun Tab1(viewModel: LyricViewModel) {
  val state = viewModel.state.collectAsState().value
  LyricScreen(
    state = state,
    event = viewModel::onEvent,
    readEvent = { event ->
      if (state.searchQuery.trim()
        .isNotBlank()
      ) viewModel.onEvent(LyricUiEvent.InsertLyricUiTag)

      when (event) {
//        is PreviewUiEvent.Click -> navController.navigate(Route.Read(event.number)) {
//          launchSingleTop = true
//        }

        else -> Unit
      }
    }
  )
}
