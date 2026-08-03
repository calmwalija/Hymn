package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route

data class BottomTab(
  val route: Route,
  val labelRes: Int,
  val icon: ImageVector,
)

val HymnBottomTabs = listOf(
  BottomTab(Route.Home, R.string.nav_home, Icons.Outlined.Home),
  BottomTab(Route.Browse, R.string.nav_browse, Icons.Outlined.Search),
  BottomTab(Route.Insights, R.string.nav_insights, Icons.Outlined.Star),
  BottomTab(Route.Library, R.string.nav_library, Icons.Outlined.FavoriteBorder),
)

@Composable
fun HymnBottomBar(
  selectedRoute: Route?,
  onSelect: (Route) -> Unit,
) {
  NavigationBar {
    HymnBottomTabs.forEach { tab ->
      val label = stringResource(tab.labelRes)
      val selected = selectedRoute?.let { it::class == tab.route::class } == true
      NavigationBarItem(
        selected = selected,
        onClick = { onSelect(tab.route) },
        icon = { Icon(tab.icon, contentDescription = label) },
        label = { Text(label) },
      )
    }
  }
}
