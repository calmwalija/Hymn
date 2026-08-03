package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import net.techandgraphics.hymn.ui.Route

data class BottomTab(
  val route: Route,
  val label: String,
  val icon: ImageVector,
)

val HymnBottomTabs = listOf(
  BottomTab(Route.Home, "Home", Icons.Outlined.Home),
  BottomTab(Route.Browse, "Browse", Icons.Outlined.MenuBook),
  BottomTab(Route.Insights, "Insights", Icons.Outlined.Insights),
  BottomTab(Route.Library, "Library", Icons.Outlined.LibraryMusic),
)

@Composable
fun HymnBottomBar(
  selectedRoute: Route?,
  onSelect: (Route) -> Unit,
) {
  NavigationBar {
    HymnBottomTabs.forEach { tab ->
      val selected = selectedRoute?.let { it::class == tab.route::class } == true
      NavigationBarItem(
        selected = selected,
        onClick = { onSelect(tab.route) },
        icon = { Icon(tab.icon, contentDescription = tab.label) },
        label = { Text(tab.label) },
      )
    }
  }
}
