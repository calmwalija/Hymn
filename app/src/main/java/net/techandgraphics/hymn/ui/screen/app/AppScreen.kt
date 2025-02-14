package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.preview.PreviewScreen
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent.GoToTheCategory
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent.PopBackStack
import net.techandgraphics.hymn.ui.screen.preview.PreviewViewModel
import net.techandgraphics.hymn.ui.screen.settings.SettingsEvent
import net.techandgraphics.hymn.ui.screen.settings.SettingsScreen
import net.techandgraphics.hymn.ui.screen.settings.SettingsViewModel
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryScreen
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryUiEvent.Favorite
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryUiEvent.ToPreview
import net.techandgraphics.hymn.ui.screen.theCategory.TheCategoryViewModel
import net.techandgraphics.hymn.ui.theme.ThemeConfigs

@Composable
fun AppScreen(
  paddingValues: PaddingValues,
  onThemeConfigs: (ThemeConfigs) -> Unit,
  navController: NavHostController = rememberNavController(),
) {
  NavHost(
    modifier = Modifier.padding(paddingValues),
    navController = navController,
    startDestination = Route.Home,
  ) {

    composable<Route.Home> {
      with(hiltViewModel<MainViewModel>()) {
        val state = state.collectAsState().value
        MainScreen(state = state, channelFlow = channelFlow) { event ->
          when (event) {
            is MainUiEvent.GotoPreview ->
              navController.navigate(Route.Preview(event.lyric.number))

            is MainUiEvent.GotoCategory ->
              navController.navigate(Route.TheCategory(event.category.lyric.categoryId))

            is MainUiEvent.MenuItem.Settings -> navController.navigate(Route.Settings)

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
            if (it is SettingsEvent.DynamicColor)
              onThemeConfigs.invoke(ThemeConfigs(dynamicColor = it.isEnabled))

            if (it is SettingsEvent.FontStyle.Apply)
              onThemeConfigs.invoke(ThemeConfigs(fontFamily = it.fontFamily))

            onEvent(it)
          },
          channelFlow = channelFlow
        )
      }
    }

    composable<Route.Preview> {
      with(hiltViewModel<PreviewViewModel>()) {
        LaunchedEffect(Unit) { invoke(it.toRoute<Route.Preview>().id) }
        val state = state.collectAsState().value
        state.currentLyric
          .takeIf { it != null }
          ?.let {
            PreviewScreen(state) { event ->
              when (event) {
                GoToTheCategory -> {
                  onEvent(PreviewUiEvent.Analytics.GotoTheCategory)
                  navController.navigate(Route.TheCategory(state.categoryId))
                }
                PopBackStack -> navController.popBackStack()
                else -> onEvent(event)
              }
            }
          }
      }
    }

    composable<Route.TheCategory> {
      with(hiltViewModel<TheCategoryViewModel>()) {
        LaunchedEffect(Unit) { invoke(it.toRoute<Route.TheCategory>().id) }
        val state = state.collectAsState().value
        TheCategoryScreen(state = state) { event ->
          when (event) {
            is Favorite -> onEvent(event)
            is ToPreview -> navController.navigate(Route.Preview(event.theHymnNumber))
          }
        }
      }
    }
  }
}
