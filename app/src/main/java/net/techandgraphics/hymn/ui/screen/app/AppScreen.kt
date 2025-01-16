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
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscScreen
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscViewModel
import net.techandgraphics.hymn.ui.screen.preview.PreviewScreen
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent
import net.techandgraphics.hymn.ui.screen.preview.PreviewViewModel

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
              is MainUiEvent.Event -> when (event.ofType) {
                MainUiEvent.OfType.Category -> {
                  onAnalyticEvent(AnalyticEvent.Spotlight(event.id))
                  navController.navigate(Route.Categorisation(event.id)) {
                    launchSingleTop = true
                  }
                }

                MainUiEvent.OfType.Preview -> {
                  onAnalyticEvent(AnalyticEvent.DiveInto(event.id))
                  navController.navigate(Route.Read(event.id)) {
                    launchSingleTop = true
                  }
                }
              }

              is MainUiEvent.CategoryUiEvent.GoTo -> {
                navController.navigate(Route.Categorisation(event.category.lyric.categoryId)) {
                  launchSingleTop = true
                }
              }

              else -> onEvent(event)
            }
          }
        }
      }

      composable<Route.Mixed> {
        with(hiltViewModel<MiscViewModel>()) {
          val state = state.collectAsState().value
          MiscScreen(
            state,
            event = ::onEvent,
            readEvent = { event ->
              when (event) {
                is PreviewUiEvent.Click -> navController.navigate(Route.Read(event.number)) {
                  launchSingleTop = true
                }

                else -> Unit
              }
            }
          )
        }
      }

      composable<Route.Read> {
        with(hiltViewModel<PreviewViewModel>()) {
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
          PreviewScreen(state, navController, ::onEvent)
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
                is PreviewUiEvent.Click -> navController.navigate(Route.Read(event.number)) {
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
