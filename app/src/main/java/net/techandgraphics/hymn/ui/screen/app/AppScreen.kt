package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationScreen
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationViewModel
import net.techandgraphics.hymn.ui.screen.main.AnalyticEvent
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscScreen
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscViewModel
import net.techandgraphics.hymn.ui.screen.read.ReadEvent
import net.techandgraphics.hymn.ui.screen.read.ReadScreen
import net.techandgraphics.hymn.ui.screen.read.ReadViewModel
import net.techandgraphics.hymn.ui.screen.searching.SearchingScreen
import net.techandgraphics.hymn.ui.screen.searching.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.searching.lyric.SearchViewModel

const val ANIMATION_DURATION = 300

@Composable
fun AppScreen(
  navController: NavHostController = rememberNavController(),
) {

  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route?.substringAfterLast(".") ?: ""

  Scaffold(
    bottomBar = {
      NavigationBar {
        bottomNavigationList.forEach { item ->
          NavigationBarItem(
            selected = currentRoute.contains(item.title),
            onClick = {
              navController.navigate(item.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
              }
            },
            label = {
              Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
              )
            },
            icon = {
              Icon(
                painter = painterResource(
                  id = if (currentRoute.contains(item.title)) item.selectedIcon else item.unSelectedIcon
                ),
                modifier = Modifier.size(18.dp),
                contentDescription = item.title
              )
            },
          )
        }
      }
    }
  ) {

    NavHost(
      navController = navController,
      startDestination = Route.Home,
      modifier = Modifier.padding(it),

    ) {

      composable<Route.Home> {
        with(hiltViewModel<MainViewModel>()) {
          LaunchedEffect(key1 = Unit) { get() }
          val state = state.collectAsState().value
          MainScreen(state = state) { event ->
            when (event) {
              is MainEvent.Event -> when (event.ofType) {
                MainEvent.OfType.Category -> {
                  onAnalyticEvent(AnalyticEvent.Spotlight(event.id))
                  navController.navigate(Route.Categorisation(event.id)) {
                    launchSingleTop = true
                  }
                }

                MainEvent.OfType.Read -> {
                  onAnalyticEvent(AnalyticEvent.DiveInto(event.id))
                  navController.navigate(Route.Read(event.id)) {
                    launchSingleTop = true
                  }
                }
              }

              is MainEvent.Goto -> when (event.navigate) {
                MainEvent.Navigate.Search -> {
                  onAnalyticEvent(AnalyticEvent.GotoSearch)
                  navController.navigate(Route.Searching())
                }

                MainEvent.Navigate.Category -> {
                  onAnalyticEvent(AnalyticEvent.GotoCategory)
                  navController.navigate(Route.Searching(1))
                }
              }

              else -> onEvent(event)
            }
          }
        }
      }

      composable<Route.Searching> {
        val searchViewModel = hiltViewModel<SearchViewModel>()
        val categoryViewModel = hiltViewModel<CategoryViewModel>()
        val activeTab = it.toRoute<Route.Searching>().tab
        SearchingScreen(activeTab, searchViewModel, categoryViewModel)
      }

      composable<Route.Mixed> {
        with(hiltViewModel<MiscViewModel>()) {
          val state = state.collectAsState().value
          MiscScreen(
            state,
            event = ::onEvent,
            readEvent = { event ->
              when (event) {
                is ReadEvent.Click -> navController.navigate(Route.Read(event.number)) {
                  launchSingleTop = true
                }

                else -> Unit
              }
            }
          )
        }
      }

      composable<Route.Read> {
        with(hiltViewModel<ReadViewModel>()) {
          LaunchedEffect(key1 = true) {
            invoke(it.toRoute<Route.Read>().id)
          }
          val state = state.collectAsState().value

          val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

          LaunchedEffect(key1 = channelFlow) {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
              channelFlow.collect {

                navController.popBackStack(Route.Read(it.old), inclusive = true)

                navController.navigate(Route.Read(it.new)) {
                  launchSingleTop = true
                }
              }
            }
          }
          ReadScreen(state, navController, ::onEvent)
        }
      }

      composable<Route.Categorisation> {
        with(hiltViewModel<CategorisationViewModel>()) {
          LaunchedEffect(key1 = true) {
            invoke(it.toRoute<Route.Categorisation>().id)
          }
          val state = state.collectAsState().value
          CategorisationScreen(
            state = state,
            event = ::onEvent,
            readEvent = { event ->
              when (event) {
                is ReadEvent.Click -> navController.navigate(Route.Read(event.number)) {
                  launchSingleTop = true
                }

                else -> Unit
              }
            }
          )
        }
      }
    }
  }
}
