package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.screen.main.components.LyricScreenItem
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun SearchEmptyState(
  paddingValues: PaddingValues,
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {

  Column(
    modifier = Modifier
      .padding(24.dp)
      .padding(paddingValues),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Icon(
      painter = painterResource(R.drawable.ic_book),
      contentDescription = null,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = "No results found",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )
    Text(
      text = "We couldn't find what you were looking for, but here are a few suggestions.",
      modifier = Modifier.fillMaxWidth(),
      textAlign = TextAlign.Center
    )

    Box(modifier = Modifier.padding(vertical = 16.dp)) {
      LazyColumn {
        itemsIndexed(
          state.emptyStateSuggestedLyrics,
          key = { _, lyric -> lyric.lyricId }
        ) { index, lyric ->
          LyricScreenItem(
            lyric = lyric,
            onEvent = { event ->
              onEvent(MainUiEvent.LyricEvent.InsertSearchTag)
              onEvent(event)
            },
            modifier = Modifier.animateItem(),
            showDivider = index.plus(1) < state.emptyStateSuggestedLyrics.size
          )
        }
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
fun SearchEmptyStatePreview() {
  HymnTheme {
    SearchEmptyState(
      paddingValues = PaddingValues(),
      state = MainUiState(
        emptyStateSuggestedLyrics = listOf(
          Faker.lyric.copy(lyricId = 12),
          Faker.lyric.copy(lyricId = 932),
          Faker.lyric.copy(lyricId = 907),
          Faker.lyric.copy(lyricId = 387),
        )
      )
    ) {
    }
  }
}
