package net.techandgraphics.hymn.ui.screen.browse

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toNumber
import net.techandgraphics.hymn.ui.screen.component.ImageComponent

@Composable
fun BrowseScreen(
  state: BrowseUiState,
  onEvent: (BrowseUiEvent) -> Unit,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Text(
      text = "Browse",
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
    )
    OutlinedTextField(
      value = state.query,
      onValueChange = { onEvent(BrowseUiEvent.Query(it)) },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      singleLine = true,
      placeholder = { Text("Search hymns") },
    )
    Row(
      modifier = Modifier
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
      BrowseSort.entries.forEach { sort ->
        FilterChip(
          selected = state.sort == sort,
          onClick = { onEvent(BrowseUiEvent.Sort(sort)) },
          label = { Text(sort.name) },
          modifier = Modifier.padding(end = 8.dp),
        )
      }
      FilterChip(
        selected = state.favoritesOnly,
        onClick = { onEvent(BrowseUiEvent.ToggleFavoritesOnly) },
        label = { Text("Favorites") },
      )
    }

    when (state.sort) {
      BrowseSort.Categories -> LazyColumn {
        items(state.categories, key = { it.lyric.categoryId }) { category ->
          CategoryBrowseRow(category) {
            onEvent(BrowseUiEvent.OpenCategory(category.lyric.categoryId))
          }
        }
      }

      else -> LazyColumn {
        items(state.hymns, key = { it.number }) { hymn ->
          HymnBrowseRow(
            lyric = hymn,
            onOpen = { onEvent(BrowseUiEvent.OpenHymn(hymn.number)) },
            onFavorite = {
              onEvent(BrowseUiEvent.Favorite(hymn.number, !hymn.favorite))
            },
          )
        }
      }
    }
  }
}

@Composable
private fun HymnBrowseRow(
  lyric: Lyric,
  onOpen: () -> Unit,
  onFavorite: () -> Unit,
) {
  Column(modifier = Modifier.clickable(onClick = onOpen)) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
    ) {
      ImageComponent(lyric)
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 12.dp),
      ) {
        Text(text = lyric.toNumber(), fontWeight = FontWeight.Bold)
        Text(
          text = lyric.title,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          color = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = lyric.categoryName,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall,
        )
      }
      IconButton(onClick = onFavorite) {
        Icon(
          imageVector = if (lyric.favorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
          contentDescription = "Favorite",
        )
      }
    }
    HorizontalDivider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
  }
}

@Composable
private fun CategoryBrowseRow(category: Category, onOpen: () -> Unit) {
  Column(modifier = Modifier.clickable(onClick = onOpen)) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
    ) {
      ImageComponent(category.lyric)
      Column(modifier = Modifier.padding(horizontal = 12.dp)) {
        Text(
          text = category.lyric.categoryName,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = category.count.substringBefore("-") + " hymns",
          style = MaterialTheme.typography.bodySmall,
        )
      }
    }
    HorizontalDivider(modifier = Modifier.padding(start = 72.dp, end = 16.dp))
  }
}
