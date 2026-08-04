package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.components.HymnListItem
import net.techandgraphics.hymn.ui.components.SectionHeader
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.Space

/**
 * Shown when a search returns nothing. It stays useful rather than dead-ending:
 * the explanation is followed by suggested hymns the reader can open directly.
 */
@Composable
fun SearchEmptyState(
  paddingValues: PaddingValues,
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(paddingValues),
  ) {
    item {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = Space.xl, vertical = Space.lg),
      ) {
        Icon(
          imageVector = Icons.Rounded.Search,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(48.dp),
        )
        Spacer(Modifier.height(Space.sm))
        Text(
          text = stringResource(R.string.search_empty_title),
          style = MaterialTheme.typography.titleLarge,
          textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(Space.xxs))
        Text(
          text = stringResource(R.string.search_empty_message),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
        )
      }
    }

    if (state.emptyStateSuggestedLyrics.isNotEmpty()) {
      item { SectionHeader(title = stringResource(R.string.home_featured)) }
      items(state.emptyStateSuggestedLyrics, key = { it.lyricId }) { lyric ->
        HymnListItem(
          lyric = lyric,
          modifier = Modifier.animateItem(),
          onClick = {
            onEvent(MainUiEvent.LyricEvent.InsertSearchTag)
            onEvent(MainUiEvent.GotoPreview(lyric))
          },
        )
      }
    }
  }
}

@PreviewLightDark
@Composable
private fun SearchEmptyStatePreview() {
  HymnTheme {
    SearchEmptyState(
      paddingValues = PaddingValues(),
      state = MainUiState(
        emptyStateSuggestedLyrics = listOf(
          Faker.lyric.copy(lyricId = 12),
          Faker.lyric.copy(lyricId = 932),
        ),
      ),
      onEvent = {},
    )
  }
}
