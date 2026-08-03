package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.browse.BrowseScreen
import net.techandgraphics.hymn.ui.screen.browse.BrowseUiEvent
import net.techandgraphics.hymn.ui.screen.browse.BrowseViewModel
import net.techandgraphics.hymn.ui.screen.insights.InsightsScreen
import net.techandgraphics.hymn.ui.screen.insights.InsightsViewModel
import net.techandgraphics.hymn.ui.screen.library.FavoritesViewModel
import net.techandgraphics.hymn.ui.screen.library.HistoryViewModel
import net.techandgraphics.hymn.ui.screen.library.HymnSimpleList
import net.techandgraphics.hymn.ui.screen.library.LibraryScreen
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.main.components.ApostleCreedDialog
import net.techandgraphics.hymn.ui.screen.main.components.LordsPrayerDialog
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
import net.techandgraphics.hymn.ui.screen.wrapped.WrappedViewModel
import net.techandgraphics.hymn.ui.screen.wrapped.YearInHymnsScreen
import net.techandgraphics.hymn.ui.theme.ThemeConfigs
import java.util.Calendar

@Composable
fun AppScreen(
  paddingValues: PaddingValues,
  onThemeConfigs: (ThemeConfigs) -> Unit,
  navController: NavHostController = rememberNavController(),
) {
  val backStackEntry by navController.currentBackStackEntryAsState()
  val destination = backStackEntry?.destination
  val selectedTab = HymnBottomTabs.firstOrNull { tab ->
    destination?.hasRoute(tab.route::class) == true
  }?.route
  val showBottomBar = destination != null && (
    destination.hasRoute<Route.Home>() ||
      destination.hasRoute<Route.Browse>() ||
      destination.hasRoute<Route.Insights>() ||
      destination.hasRoute<Route.Library>()
    )

  Scaffold(
    modifier = Modifier.padding(paddingValues),
    bottomBar = {
      if (showBottomBar) {
        HymnBottomBar(
          selectedRoute = selectedTab,
          onSelect = { route ->
            navController.navigate(route) {
              popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
              }
              launchSingleTop = true
              restoreState = true
            }
          },
        )
      }
    },
  ) { innerPadding ->
    NavHost(
      modifier = Modifier.padding(innerPadding),
      navController = navController,
      startDestination = Route.Home,
    ) {
      composable<Route.Home> {
        with(hiltViewModel<MainViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
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

      composable<Route.Browse> {
        with(hiltViewModel<BrowseViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
          BrowseScreen(state = state) { event ->
            when (event) {
              is BrowseUiEvent.OpenHymn ->
                navController.navigate(Route.Preview(event.number))

              is BrowseUiEvent.OpenCategory ->
                navController.navigate(Route.TheCategory(event.categoryId))

              else -> onEvent(event)
            }
          }
        }
      }

      composable<Route.Insights> {
        with(hiltViewModel<InsightsViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
          InsightsScreen(
            state = state,
            onToggleYear = ::toggleYear,
            onOpenHymn = { navController.navigate(Route.Preview(it)) },
            onYearInHymns = {
              navController.navigate(Route.YearInHymns(Calendar.getInstance().get(Calendar.YEAR)))
            },
          )
        }
      }

      composable<Route.Library> {
        with(hiltViewModel<MainViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
          var showCreed by remember { mutableStateOf(false) }
          var showPrayer by remember { mutableStateOf(false) }
          if (showCreed) ApostleCreedDialog(state, ::onEvent) { showCreed = false }
          if (showPrayer) LordsPrayerDialog(state, ::onEvent) { showPrayer = false }
          LibraryScreen(
            onFavorites = { navController.navigate(Route.Favorites) },
            onHistory = { navController.navigate(Route.History) },
            onSettings = { navController.navigate(Route.Settings) },
            onCreed = { showCreed = true },
            onPrayer = { showPrayer = true },
          )
        }
      }

      composable<Route.Favorites> {
        with(hiltViewModel<FavoritesViewModel>()) {
          val favorites = favorites.collectAsStateWithLifecycle().value
          HymnSimpleList(
            title = "Favorites",
            hymns = favorites,
            emptyMessage = "No favorites yet. Heart a hymn while reading.",
            onOpen = { navController.navigate(Route.Preview(it)) },
          )
        }
      }

      composable<Route.History> {
        with(hiltViewModel<HistoryViewModel>()) {
          val history = history.collectAsStateWithLifecycle().value
          HymnSimpleList(
            title = "History",
            hymns = history,
            emptyMessage = "Open a hymn to start building your history.",
            onOpen = { navController.navigate(Route.Preview(it)) },
          )
        }
      }

      composable<Route.YearInHymns> {
        with(hiltViewModel<WrappedViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
          YearInHymnsScreen(
            state = state,
            onClose = { navController.popBackStack() },
          )
        }
      }

      composable<Route.Settings> {
        with(hiltViewModel<SettingsViewModel>()) {
          val state = state.collectAsStateWithLifecycle().value
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
          val state = state.collectAsStateWithLifecycle().value
          state.currentLyric
            .takeIf { lyric -> lyric != null }
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
          val state = state.collectAsStateWithLifecycle().value
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
}
