package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
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
import net.techandgraphics.hymn.ui.Route.Categorisation
import net.techandgraphics.hymn.ui.Route.Category
import net.techandgraphics.hymn.ui.Route.Home
import net.techandgraphics.hymn.ui.Route.Miscellaneous
import net.techandgraphics.hymn.ui.Route.Read
import net.techandgraphics.hymn.ui.Route.Search
import net.techandgraphics.hymn.ui.screen.Event
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationScreen
import net.techandgraphics.hymn.ui.screen.categorisation.CategorisationViewModel
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent
import net.techandgraphics.hymn.ui.screen.category.CategoryScreen
import net.techandgraphics.hymn.ui.screen.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.main.AnalyticEvent
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.screen.main.MainNavigator
import net.techandgraphics.hymn.ui.screen.main.MainScreen
import net.techandgraphics.hymn.ui.screen.main.MainViewModel
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscScreen
import net.techandgraphics.hymn.ui.screen.miscellaneous.MiscViewModel
import net.techandgraphics.hymn.ui.screen.read.ReadEvent
import net.techandgraphics.hymn.ui.screen.read.ReadScreen
import net.techandgraphics.hymn.ui.screen.read.ReadViewModel
import net.techandgraphics.hymn.ui.screen.search.SearchEvent
import net.techandgraphics.hymn.ui.screen.search.SearchScreen
import net.techandgraphics.hymn.ui.screen.search.SearchViewModel

const val ANIMATION_DURATION = 300

@Composable
fun AppScreen(
  navController: NavHostController = rememberNavController(),
) {

  val backStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = backStackEntry?.destination?.route?.run {
    if (contains(Category.title) || contains(Categorisation.route) || contains(Read.title))
      Home.title else this.capitalizeFirst()
  }

  Scaffold(
    bottomBar = {
      NavigationBar {
        bottomNavigationList.map { it.copy(title = it.title.capitalizeFirst()) }
          .forEach { item ->
            NavigationBarItem(
              selected = currentRoute == item.title,
              onClick = {
                navController.navigate(item.title) {
                  popUpTo(navController.graph.findStartDestination().id)
                  launchSingleTop = true
                }
              },
              label = {
                AnimatedVisibility(visible = currentRoute == item.title) {
                  Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                  )
                }
              },
              icon = {
                Icon(
                  painter = painterResource(
                    id = if (currentRoute == item.title) item.selectedIcon else item.unSelectedIcon
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
      startDestination = Home.title,
      modifier = Modifier.padding(it)
    ) {

      composable(
        route = Home.title,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        }
      ) {
        val mainViewModel: MainViewModel = hiltViewModel()
        val state = mainViewModel.state.collectAsState().value
        MainScreen(
          mainEvent = mainViewModel::onEvent,
          state = state,
          categoryEvent = { event ->
            mainViewModel.onAnalyticEvent(AnalyticEvent.Spotlight((event as CategoryEvent.Click).categoryId))
            navController.navigate(Event.category(event)) {
              launchSingleTop = true
            }
          },
          readEvent = { event ->
            mainViewModel.onAnalyticEvent(AnalyticEvent.DiveInto((event as ReadEvent.Click).number))
            navController.navigate(Event.read(event)) {
              launchSingleTop = true
            }
          },
          navigator = { navigation ->
            when (navigation) {
              MainNavigator.NavigateToCategory -> {
                mainViewModel.onAnalyticEvent(AnalyticEvent.GotoCategory)
                navController.navigate(Category.title)
              }

              MainNavigator.NavigateToSearch -> {
                mainViewModel.onAnalyticEvent(AnalyticEvent.GotoCategory)
                navController.navigate(Search.title)
              }
            }
          }
        ) { lang ->
          mainViewModel.onEvent(MainEvent.LanguageChange(lang))
        }
      }

      composable(
        route = Category.title,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        }
      ) {
        val categoryViewModel: CategoryViewModel = hiltViewModel()
        val state = categoryViewModel.state.collectAsState().value
        CategoryScreen(state) { event ->
          navController.navigate(Event.category(event)) {
            launchSingleTop = true
          }
        }
      }

      composable(
        route = Search.title,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        }
      ) {
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

      composable(
        route = Miscellaneous.title,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        }
      ) {
        val miscViewModel: MiscViewModel = hiltViewModel()
        val state = miscViewModel.state.collectAsState().value
        MiscScreen(
          state,
          event = miscViewModel::onEvent,
          readEvent = { event ->
            navController.navigate(Event.read(event)) {
              launchSingleTop = true
            }
          }
        )
      }

      composable(
        route = Read.route,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        arguments = listOf(navArgument("id") { type = NavType.IntType })
      ) {
        val lyricId = backStackEntry?.arguments?.getInt("id") ?: 1
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
        route = Categorisation.route,
        enterTransition = {
          slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        exitTransition = {
          slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = tween(ANIMATION_DURATION)
          )
        },
        arguments = listOf(navArgument("id") { type = NavType.IntType })
      ) {
        val categoryId = backStackEntry?.arguments?.getInt("id") ?: 1
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
