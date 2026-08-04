package net.techandgraphics.hymn.ui.screen.app

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

data class BottomTab(
  val route: Route,
  val labelRes: Int,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
)

/**
 * Four top-level destinations. Library used to exist in the graph with no way
 * to reach it; it is a tab now, and Settings lives inside it rather than
 * occupying a tab of its own.
 */
val HymnBottomTabs = listOf(
  BottomTab(Route.Home, R.string.nav_home, Icons.Rounded.Home, Icons.Outlined.Home),
  BottomTab(Route.Browse, R.string.nav_browse, Icons.Rounded.Search, Icons.Outlined.Search),
  BottomTab(Route.Library, R.string.nav_library, Icons.AutoMirrored.Rounded.List, Icons.AutoMirrored.Outlined.List),
  BottomTab(Route.Insights, R.string.nav_insights, Icons.Rounded.Star, Icons.Outlined.Star),
)

/** Extra space under scroll content so lists clear the floating capsule. */
val FloatingNavContentPadding = 92.dp

@Composable
fun HymnBottomBar(
  selectedRoute: Route?,
  onSelect: (Route) -> Unit,
  modifier: Modifier = Modifier,
) {
  // The parent (AppScreen's root Scaffold) already insets its whole content
  // area above the system navigation bar via `WindowInsets.safeDrawing`.
  // Applying `WindowInsets.navigationBars` again here double-counted that
  // inset, floating the capsule well above the true bottom edge and leaving a
  // plain, sharp-cornered strip of background beneath it that read as a
  // second bar sitting behind the rounded one.
  Box(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = Space.sm, vertical = Space.xs),
    contentAlignment = Alignment.Center,
  ) {
    Surface(
      shape = PillShape,
      color = MaterialTheme.colorScheme.surfaceContainer,
      tonalElevation = 3.dp,
      shadowElevation = 8.dp,
      modifier = Modifier
        .fillMaxWidth()
        .height(Sizes.navBar),
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = Space.xxs),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        HymnBottomTabs.forEach { tab ->
          val label = stringResource(tab.labelRes)
          val selected = selectedRoute?.let { it::class == tab.route::class } == true
          // Each tab claims an equal share of the row and centers within it,
          // rather than SpaceEvenly's gap calculated off the widest item —
          // that put a visibly bigger gap in front of Home whenever its
          // selected (wider) pill was the first item in the row.
          Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            FloatingNavItem(
              selected = selected,
              icon = if (selected) tab.selectedIcon else tab.unselectedIcon,
              label = label,
              onClick = { onSelect(tab.route) },
            )
          }
        }
      }
    }
  }
}

@Composable
private fun FloatingNavItem(
  selected: Boolean,
  icon: ImageVector,
  label: String,
  onClick: () -> Unit,
) {
  // The selected tab grows a labelled pill; the others stay as bare icons.
  // Animating the width keeps the row from jumping as selection moves.
  val wellWidth by animateDpAsState(if (selected) 96.dp else 56.dp, label = "navWellWidth")
  val contentColor by animateColorAsState(
    if (selected) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onSurfaceVariant,
    label = "navTint",
  )
  val wellColor by animateColorAsState(
    if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.surfaceContainer,
    label = "navWell",
  )

  Box(
    modifier = Modifier
      .width(wellWidth)
      .height(Sizes.minTouchTarget)
      .clip(PillShape)
      .background(wellColor)
      .semantics {
        contentDescription = label
        role = Role.Tab
        this.selected = selected
      }
      .clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = onClick,
      ),
    contentAlignment = Alignment.Center,
  ) {
    if (selected) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
      ) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = contentColor,
          modifier = Modifier.size(20.dp),
        )
        Text(
          text = label,
          style = MaterialTheme.typography.labelMedium,
          color = contentColor,
          maxLines = 1,
          modifier = Modifier.padding(start = Space.xxs),
        )
      }
    } else {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = icon,
          contentDescription = null,
          tint = contentColor,
          modifier = Modifier.size(Sizes.icon),
        )
      }
    }
  }
}
