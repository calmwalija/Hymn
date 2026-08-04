package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.screen.main.components.CategoryGridItem
import net.techandgraphics.hymn.ui.theme.Space

/**
 * All hymnal themes as a two-column grid of artwork cards. This replaces the
 * modal bottom sheet the home screen used to open, which offered no title, no
 * scroll context and no way to reach a theme from anywhere else.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
  categories: List<Category>,
  onBack: () -> Unit,
  onEvent: (MainUiEvent) -> Unit,
) {
  val scrollBehavior =
    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.categories_title),
        onBack = onBack,
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    if (categories.isEmpty()) {
      EmptyState(
        icon = Icons.AutoMirrored.Rounded.List,
        title = stringResource(R.string.categories_empty_title),
        message = stringResource(R.string.categories_empty_message),
        modifier = Modifier.padding(padding),
      )
      return@Scaffold
    }

    LazyVerticalGrid(
      columns = GridCells.Fixed(2),
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentPadding = PaddingValues(
        start = Space.md,
        end = Space.md,
        top = Space.xs,
        bottom = Space.lg,
      ),
      horizontalArrangement = Arrangement.spacedBy(Space.sm),
      verticalArrangement = Arrangement.spacedBy(Space.sm),
    ) {
      items(categories, key = { it.lyric.categoryId }) { category ->
        CategoryGridItem(category = category, onEvent = onEvent)
      }
    }
  }
}
