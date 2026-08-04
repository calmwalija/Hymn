package net.techandgraphics.hymn.ui.screen.library

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnListItem
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NavigationRow
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

/**
 * The Library hub. This screen already existed in the navigation graph but had
 * no entry point anywhere in the app; it is now a bottom-navigation tab and the
 * home for Settings and About.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
  onFavorites: () -> Unit,
  onHistory: () -> Unit,
  onCategories: () -> Unit,
  onSettings: () -> Unit,
  onAbout: () -> Unit,
  onCreed: () -> Unit,
  onPrayer: () -> Unit,
) {
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.nav_library),
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState()),
    ) {
      NavigationRow(
        title = stringResource(R.string.favorites),
        subtitle = stringResource(R.string.library_favorites_subtitle),
        icon = Icons.Outlined.FavoriteBorder,
        onClick = onFavorites,
      )
      NavigationRow(
        title = stringResource(R.string.history),
        subtitle = stringResource(R.string.library_history_subtitle),
        icon = Icons.Outlined.DateRange,
        onClick = onHistory,
      )
      NavigationRow(
        title = stringResource(R.string.categories_title),
        subtitle = stringResource(R.string.library_themes_subtitle),
        icon = Icons.AutoMirrored.Rounded.List,
        onClick = onCategories,
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = Space.xs))

      NavigationRow(
        title = stringResource(R.string.library_creed),
        subtitle = stringResource(R.string.library_creed_subtitle),
        leading = { RowIcon(R.drawable.ic_creed) },
        onClick = onCreed,
      )
      NavigationRow(
        title = stringResource(R.string.library_prayer),
        subtitle = stringResource(R.string.library_prayer_subtitle),
        leading = { RowIcon(R.drawable.ic_prayer) },
        onClick = onPrayer,
      )

      HorizontalDivider(modifier = Modifier.padding(vertical = Space.xs))

      NavigationRow(
        title = stringResource(R.string.nav_settings),
        subtitle = stringResource(R.string.library_settings_subtitle),
        icon = Icons.Outlined.Settings,
        onClick = onSettings,
      )
      NavigationRow(
        title = stringResource(R.string.library_about),
        subtitle = stringResource(R.string.library_about_subtitle),
        icon = Icons.Outlined.Info,
        onClick = onAbout,
      )
    }
  }
}

@Composable
private fun RowIcon(@DrawableRes id: Int) {
  Icon(
    painter = painterResource(id),
    contentDescription = null,
    tint = MaterialTheme.colorScheme.primary,
    modifier = Modifier.size(Sizes.icon),
  )
}

/**
 * Shared collection screen for Favorites and History — same chrome, same row
 * treatment, different data and empty copy.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnCollectionScreen(
  title: String,
  hymns: List<Lyric>,
  emptyTitle: String,
  emptyMessage: String,
  onOpen: (Int) -> Unit,
  onBack: () -> Unit,
) {
  val scrollBehavior =
    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = title,
        onBack = onBack,
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    if (hymns.isEmpty()) {
      EmptyState(
        icon = Icons.Outlined.FavoriteBorder,
        title = emptyTitle,
        message = emptyMessage,
        modifier = Modifier.padding(padding),
      )
      return@Scaffold
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentPadding = PaddingValues(bottom = Space.lg),
    ) {
      item {
        Text(
          text = stringResource(R.string.hymn_count_label, hymns.size),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xxs),
        )
      }
      items(hymns, key = { it.number }) { lyric ->
        HymnListItem(
          lyric = lyric,
          onClick = { onOpen(lyric.number) },
        )
      }
    }
  }
}
