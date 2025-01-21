package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
import net.techandgraphics.hymn.ui.theme.ThemeConfigs

@Composable
fun AppScreen(
  onThemeConfigs: (ThemeConfigs) -> Unit,
  navController: NavHostController = rememberNavController(),
) {
  NavHost(
    navController = navController,
    startDestination = Route.Home,
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
          onThemeConfigs = onThemeConfigs,
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
