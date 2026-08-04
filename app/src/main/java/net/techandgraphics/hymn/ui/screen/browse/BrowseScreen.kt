package net.techandgraphics.hymn.ui.screen.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.components.CategoryArtwork
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnListItem
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
  state: BrowseUiState,
  onEvent: (BrowseUiEvent) -> Unit,
) {
  val scrollBehavior =
    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.nav_browse),
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
    ) {
      BrowseSearchField(query = state.query, onEvent = onEvent)
      BrowseFilters(state = state, onEvent = onEvent)
      BrowseResults(state = state, onEvent = onEvent)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BrowseSearchField(
  query: String,
  onEvent: (BrowseUiEvent) -> Unit,
) {
  TextField(
    value = query,
    onValueChange = { onEvent(BrowseUiEvent.Query(it)) },
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = Space.md),
    singleLine = true,
    shape = PillShape,
    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
    leadingIcon = {
      Icon(
        imageVector = Icons.Rounded.Search,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    },
    trailingIcon = {
      if (query.isNotEmpty()) {
        IconButton(onClick = { onEvent(BrowseUiEvent.Query("")) }) {
          Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = stringResource(R.string.action_clear),
          )
        }
      }
    },
    placeholder = { Text(stringResource(R.string.search_hymns)) },
    // An underline beneath a pill reads as a mistake; drop the indicator and
    // let the filled container carry the affordance.
    colors = TextFieldDefaults.colors(
      focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent,
    ),
  )
}

@Composable
private fun BrowseFilters(
  state: BrowseUiState,
  onEvent: (BrowseUiEvent) -> Unit,
) {
  LazyRowOfChips {
    BrowseSort.entries.forEach { sort ->
      FilterChip(
        selected = state.sort == sort,
        onClick = { onEvent(BrowseUiEvent.Sort(sort)) },
        label = { Text(stringResource(sort.labelRes())) },
        shape = PillShape,
        border = FilterChipDefaults.filterChipBorder(
          enabled = true,
          selected = state.sort == sort,
          borderColor = MaterialTheme.colorScheme.outlineVariant,
        ),
      )
    }
    FilterChip(
      selected = state.favoritesOnly,
      onClick = { onEvent(BrowseUiEvent.ToggleFavoritesOnly) },
      label = { Text(stringResource(R.string.browse_favorites_only)) },
      shape = PillShape,
      border = FilterChipDefaults.filterChipBorder(
        enabled = true,
        selected = state.favoritesOnly,
        borderColor = MaterialTheme.colorScheme.outlineVariant,
      ),
    )
  }
}

@Composable
private fun LazyRowOfChips(content: @Composable () -> Unit) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(Space.xs),
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    content()
  }
}

@Composable
private fun BrowseResults(
  state: BrowseUiState,
  onEvent: (BrowseUiEvent) -> Unit,
) {
  val isCategories = state.sort == BrowseSort.Categories
  val count = if (isCategories) state.categories.size else state.hymns.size

  if (count == 0) {
    EmptyState(
      icon = Icons.Rounded.Search,
      title = stringResource(R.string.browse_empty_title),
      message = stringResource(R.string.browse_empty_message),
    )
    return
  }

  LazyColumn(contentPadding = PaddingValues(bottom = Space.lg)) {
    item {
      Text(
        text = stringResource(R.string.search_results_count, count),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xxs),
      )
    }
    if (isCategories) {
      items(state.categories, key = { it.lyric.categoryId }) { category ->
        CategoryBrowseRow(category) {
          onEvent(BrowseUiEvent.OpenCategory(category.lyric.categoryId))
        }
      }
    } else {
      items(state.hymns, key = { it.number }) { hymn ->
        HymnListItem(
          lyric = hymn,
          onClick = { onEvent(BrowseUiEvent.OpenHymn(hymn.number)) },
          onFavorite = { onEvent(BrowseUiEvent.Favorite(hymn.number, !hymn.favorite)) },
        )
      }
    }
  }
}

@Composable
private fun CategoryBrowseRow(category: Category, onOpen: () -> Unit) {
  val context = LocalContext.current
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onClick = onOpen)
      .heightIn(min = Sizes.minTouchTarget)
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    CategoryArtwork(
      categoryId = category.lyric.categoryId,
      modifier = Modifier.size(Sizes.listArt),
    )
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = Space.sm),
    ) {
      Text(
        text = category.lyric.categoryName,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = category.count.hymnCount(context),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
    Icon(
      imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(20.dp),
    )
  }
}

private fun BrowseSort.labelRes(): Int = when (this) {
  BrowseSort.Number -> R.string.browse_sort_number
  BrowseSort.Title -> R.string.browse_sort_title
  BrowseSort.Categories -> R.string.browse_sort_categories
}
