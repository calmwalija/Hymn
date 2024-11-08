package net.techandgraphics.hymn.ui.screen.searching

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
import net.techandgraphics.hymn.ui.screen.searching.category.CategoryScreen
import net.techandgraphics.hymn.ui.screen.searching.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.searching.lyric.SearchScreen
import net.techandgraphics.hymn.ui.screen.searching.lyric.SearchViewModel

open class ToggleSwitchSearchItem : ToggleSwitchItem {
  data object Category : ToggleSwitchSearchItem()
  data object Hymn : ToggleSwitchSearchItem()
}

@Composable
fun SearchingScreen(
  activeTab: Int,
  searchViewModel: SearchViewModel,
  categoryViewModel: CategoryViewModel,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    var tabSelected by remember { mutableIntStateOf(activeTab) }
    ToggleSwitch(
      title = "Quick Search",
      toggleSwitchItems = ToggleSwitchSearchItem::class.nestedClasses.sortedByDescending { it.simpleName },
      tabSelected = tabSelected
    ) { tabSelected = it }
    if (tabSelected == 0) Tab1(viewModel = searchViewModel) else Tab2(viewModel = categoryViewModel)
  }
}

@Composable
fun Tab2(viewModel: CategoryViewModel) {
  val state = viewModel.state.collectAsState().value
  CategoryScreen(state) { event ->
    when (event) {
//      is CategoryEvent.Click ->
//        navController.navigate(Route.Categorisation(event.id)) {
//          launchSingleTop = true
//        }

      else -> viewModel.onEvent(event)
    }
  }
}

@Composable
fun Tab1(viewModel: SearchViewModel) {
  val state = viewModel.state.collectAsState().value
  SearchScreen(
    state = state,
    event = viewModel::onEvent,
    readEvent = { event ->
      if (state.searchQuery.trim()
        .isNotBlank()
      ) viewModel.onEvent(net.techandgraphics.hymn.ui.screen.searching.lyric.SearchEvent.InsertSearchTag)

      when (event) {
//        is ReadEvent.Click -> navController.navigate(Route.Read(event.number)) {
//          launchSingleTop = true
//        }

        else -> Unit
      }
    }
  )
}
