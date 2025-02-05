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
import net.techandgraphics.hymn.ui.screen.main.AnalyticEvent
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.preview.PreviewScreen
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent
import net.techandgraphics.hymn.ui.screen.preview.PreviewViewModel
import net.techandgraphics.hymn.ui.screen.settings.SettingsScreen
import net.techandgraphics.hymn.ui.screen.settings.SettingsUiEvent
import net.techandgraphics.hymn.ui.screen.settings.SettingsViewModel
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryScreen
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryViewModel
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
        val state = state.collectAsState().value
        MainScreen(state = state, channelFlow = channelFlow) { event ->
          when (event) {
            is MainUiEvent.Event -> when (event.ofType) {
              MainUiEvent.OfType.Category -> {
                onAnalyticEvent(AnalyticEvent.Spotlight(event.id))
                navController.navigate(Route.TheCategory(event.id)) {
                  launchSingleTop = true
                }
              }

              MainUiEvent.OfType.Preview -> {
                onAnalyticEvent(AnalyticEvent.DiveInto(event.id))
                navController.navigate(Route.Preview(event.id)) {
                  launchSingleTop = true
                }
              }
            }

            is MainUiEvent.CategoryUiEvent.GoTo -> {
              navController.navigate(Route.TheCategory(event.category.lyric.categoryId)) {
                launchSingleTop = true
              }
            }

            is MainUiEvent.MenuItem.Settings -> {
              navController.navigate(Route.Settings) {
                launchSingleTop = true
              }
            }

            else -> onEvent(event)
          }
        }
      }
    }

    composable<Route.Settings> {
      with(hiltViewModel<SettingsViewModel>()) {
        val state = state.collectAsState().value
        SettingsScreen(
          state = state,
          onEvent = {
            if (it is SettingsUiEvent.DynamicColor)
              onThemeConfigs.invoke(ThemeConfigs(dynamicColor = it.isEnabled))

            if (it is SettingsUiEvent.Font.Apply)
              onThemeConfigs.invoke(ThemeConfigs(fontFamily = it.fontFamily))

            onEvent(it)
          },
          channelFlow
        )
      }
    }

    composable<Route.Preview> {
      with(hiltViewModel<PreviewViewModel>()) {
        LaunchedEffect(key1 = true) {
          invoke(it.toRoute<Route.Preview>().id)
        }
        val state = state.collectAsState().value
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
        LaunchedEffect(key1 = channelFlow) {
          lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            channelFlow.collect {
              with(navController) {
                popBackStack(Route.Preview(it.old), inclusive = true)
                navigate(Route.Preview(it.new)) {
                  launchSingleTop = true
                }
              }
            }
          }
        }
        PreviewScreen(state, navController, ::onEvent)
      }
    }

    composable<Route.TheCategory> {
      with(hiltViewModel<TheCategoryViewModel>()) {
        LaunchedEffect(key1 = true) {
          invoke(it.toRoute<Route.TheCategory>().id)
        }
        val state = state.collectAsState().value
        TheCategoryScreen(
          state = state,
          onEvent = ::onEvent,
          onPreviewUiEvent = { event ->
            when (event) {
              is PreviewUiEvent.Click -> navController.navigate(Route.Preview(event.number)) {
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
