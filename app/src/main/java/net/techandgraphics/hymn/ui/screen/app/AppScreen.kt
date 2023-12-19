package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import net.techandgraphics.hymn.capitalizeFirst
import net.techandgraphics.hymn.onLanguageChange
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.Event
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationScreen
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationViewModel
import net.techandgraphics.hymn.ui.screen.category.CategoryScreen
import net.techandgraphics.hymn.ui.screen.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.screen.main.MainNavigator
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscellaneousScreen
import net.techandgraphics.hymn.ui.screen.read.ReadScreen
import net.techandgraphics.hymn.ui.screen.read.ReadViewModel
import net.techandgraphics.hymn.ui.screen.search.SearchEvent
import net.techandgraphics.hymn.ui.screen.search.SearchScreen
import net.techandgraphics.hymn.ui.screen.search.SearchViewModel

@Composable
fun AppScreen(
  navController: NavHostController = rememberNavController(),
) {

  var selectedItem by rememberSaveable { mutableIntStateOf(0) }
  val backStackEntry by navController.currentBackStackEntryAsState()

  Scaffold(
    bottomBar = {
      NavigationBar {
        bottomNavigationList.map { it.copy(title = it.title.capitalizeFirst()) }
          .forEachIndexed { index, item ->
            NavigationBarItem(
              selected = selectedItem == index,
              onClick = {
                selectedItem = index
                navController.navigate(item.title) {
                  popUpTo(navController.graph.findStartDestination().id)
                  launchSingleTop = true
                }
              },
              label = {
                AnimatedVisibility(visible = selectedItem == index) {
                  Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                  )
                }
              },
              icon = {
                Icon(
                  painter = painterResource(
                    id = if (selectedItem == index) item.selectedIcon else item.unSelectedIcon
                  ),
                  contentDescription = item.title
                )
              },
              colors = NavigationBarItemDefaults.colors()
            )
          }
      }
    }
  ) {

    NavHost(
      navController = navController,
      startDestination = Route.Home.title,
      modifier = Modifier.padding(it)
    ) {

      composable(route = Route.Home.title) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val state = mainViewModel.state.collectAsState().value
        MainScreen(
          mainEvent = mainViewModel::onEvent,
          state = state,
          categoryEvent = { event ->
            navController.navigate(Event.category(event)) {
              launchSingleTop = true
            }
          },
          readEvent = { event ->
            navController.navigate(Event.read(event)) {
              launchSingleTop = true
            }
          },
          navigator = { navigation ->
            when (navigation) {
              MainNavigator.NavigateToCategory -> navController.navigate(Route.Category.title)
              MainNavigator.NavigateToSearch -> navController.navigate(Route.Search.title)
            }
          }
        ) { lang ->
          mainViewModel.onEvent(
            MainEvent.LanguageChange(
              lang,
              onFinish = {
                navController onLanguageChange lang
              }
            )
          )
        }
      }

      composable(route = Route.Category.title) {
        val categoryViewModel: CategoryViewModel = hiltViewModel()
        val state = categoryViewModel.state.collectAsState().value
        CategoryScreen(state) { event ->
          navController.navigate(Event.category(event)) {
            launchSingleTop = true
          }
        }
      }

      composable(route = Route.Search.title) {
        val searchViewModel: SearchViewModel = hiltViewModel()
        with(searchViewModel) {
          val state = state.collectAsState().value
          SearchScreen(
            state = state,
            event = this::onEvent,
            readEvent = { event ->
              if (state.searchQuery.trim().isNotBlank()) onEvent(SearchEvent.InsertSearchTag)
              navController.navigate(Event.read(event)) {
                launchSingleTop = true
              }
            }
          )
        }
      }

      composable(route = Route.Miscellaneous.title) {
        MiscellaneousScreen()
      }

      composable(
        route = Route.Read.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
      ) {
        val lyricId = backStackEntry?.arguments?.getInt("id") ?: 4
        val readViewModel: ReadViewModel = hiltViewModel()
        with(readViewModel) {
          LaunchedEffect(key1 = true) {
            readViewModel.invoke(lyricId)
          }
          val state = state.collectAsState().value
          ReadScreen(state, navController, readViewModel::onEvent)
        }
      }

      composable(
        route = Route.Categorisation.route,
        arguments = listOf(navArgument("id") { type = NavType.IntType })
      ) {
        val categoryId = backStackEntry?.arguments?.getInt("id") ?: 0
        val categorisationViewModel: CategorisationViewModel = hiltViewModel()
        with(categorisationViewModel) {
          LaunchedEffect(key1 = true) {
            categorisationViewModel.invoke(categoryId)
          }
          val state = state.collectAsState().value
          CategorisationScreen(
            state = state,
            event = categorisationViewModel::onEvent,
            readEvent = { event ->
              navController.navigate(Event.read(event)) {
                launchSingleTop = true
              }
            }
          )
        }
      }
    }
  }
}
