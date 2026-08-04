package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.about.AboutScreen
import net.techandgraphics.hymn.ui.screen.browse.BrowseScreen
import net.techandgraphics.hymn.ui.screen.browse.BrowseUiEvent
import net.techandgraphics.hymn.ui.screen.browse.BrowseViewModel
import net.techandgraphics.hymn.ui.screen.category.CategoriesScreen
import net.techandgraphics.hymn.ui.screen.insights.InsightsScreen
import net.techandgraphics.hymn.ui.screen.insights.InsightsViewModel
import net.techandgraphics.hymn.ui.screen.library.FavoritesViewModel
import net.techandgraphics.hymn.ui.screen.library.HistoryViewModel
import net.techandgraphics.hymn.ui.screen.library.HymnCollectionScreen
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppScreen(
  onThemeConfigs: (ThemeConfigs) -> Unit,
  navController: NavHostController = rememberNavController(),
) {
  val backStackEntry by navController.currentBackStackEntryAsState()
  val destination = backStackEntry?.destination
  val selectedTab = HymnBottomTabs.firstOrNull { tab ->
    destination?.hasRoute(tab.route::class) == true
  }?.route

  // The capsule is bottom-aligned inside a box that shrinks for the keyboard,
  // so while typing it would ride up and hover over the search results. Hide it
  // for the duration and give the results the full screen.
  val showBottomBar = selectedTab != null && !WindowInsets.isImeVisible

  /** Switch top-level tabs without stacking them on the back stack. */
  fun switchTab(route: Route) = navController.navigate(route) {
    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
  }

  Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      NavHost(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = if (showBottomBar) FloatingNavContentPadding else 0.dp),
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
                is MainUiEvent.MenuItem.Favorites -> navController.navigate(Route.Favorites)
                MainUiEvent.GotoCategories -> navController.navigate(Route.Categories)

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
              onCategories = { navController.navigate(Route.Categories) },
              onSettings = { navController.navigate(Route.Settings) },
              onAbout = { navController.navigate(Route.About) },
              onCreed = { showCreed = true },
              onPrayer = { showPrayer = true },
            )
          }
        }

        composable<Route.Categories> {
          with(hiltViewModel<MainViewModel>()) {
            val state = state.collectAsStateWithLifecycle().value
            CategoriesScreen(
              categories = state.categories,
              onBack = { navController.popBackStack() },
              onEvent = { event ->
                if (event is MainUiEvent.GotoCategory) {
                  navController.navigate(Route.TheCategory(event.category.lyric.categoryId))
                } else {
                  onEvent(event)
                }
              },
            )
          }
        }

        composable<Route.Favorites> {
          with(hiltViewModel<FavoritesViewModel>()) {
            val favorites = favorites.collectAsStateWithLifecycle().value
            HymnCollectionScreen(
              title = stringResource(R.string.favorites),
              hymns = favorites,
              emptyTitle = stringResource(R.string.favorites_empty_title),
              emptyMessage = stringResource(R.string.favorites_empty_message),
              onOpen = { navController.navigate(Route.Preview(it)) },
              onBack = { navController.popBackStack() },
            )
          }
        }

        composable<Route.History> {
          with(hiltViewModel<HistoryViewModel>()) {
            val history = history.collectAsStateWithLifecycle().value
            HymnCollectionScreen(
              title = stringResource(R.string.history),
              hymns = history,
              emptyTitle = stringResource(R.string.history_empty_title),
              emptyMessage = stringResource(R.string.history_empty_message),
              onOpen = { navController.navigate(Route.Preview(it)) },
              onBack = { navController.popBackStack() },
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
              onBack = { navController.popBackStack() },
              onEvent = {
                if (it is SettingsEvent.DynamicColor)
                  onThemeConfigs.invoke(ThemeConfigs(dynamicColor = it.isEnabled))

                if (it is SettingsEvent.FontStyle.Apply)
                  onThemeConfigs.invoke(ThemeConfigs(fontFamily = it.fontFamily))

                if (it is SettingsEvent.ThemeMode)
                  onThemeConfigs.invoke(ThemeConfigs(appTheme = it.theme))

                onEvent(it)
              },
              channelFlow = channelFlow,
            )
          }
        }

        composable<Route.About> {
          with(hiltViewModel<SettingsViewModel>()) {
            val state = state.collectAsStateWithLifecycle().value
            AboutScreen(
              hymnCount = state.hymnCount,
              onBack = { navController.popBackStack() },
            )
          }
        }

        composable<Route.Preview> {
          with(hiltViewModel<PreviewViewModel>()) {
            LaunchedEffect(Unit) { invoke(it.toRoute<Route.Preview>().id) }
            val state = state.collectAsStateWithLifecycle().value
            if (state.currentLyric != null) {
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
            TheCategoryScreen(
              state = state,
              onBack = { navController.popBackStack() },
            ) { event ->
              when (event) {
                is Favorite -> onEvent(event)
                is ToPreview -> navController.navigate(Route.Preview(event.theHymnNumber))
              }
            }
          }
        }
      }

      // The capsule slides away on detail screens instead of blinking out, so
      // moving between a tab and a hymn does not flash the layout.
      AnimatedVisibility(
        visible = showBottomBar,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it },
        modifier = Modifier.align(Alignment.BottomCenter),
      ) {
        HymnBottomBar(
          selectedRoute = selectedTab,
          onSelect = ::switchTab,
        )
      }
    }
  }
}
