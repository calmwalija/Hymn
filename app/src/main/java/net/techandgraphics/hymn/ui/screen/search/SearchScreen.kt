package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.screen.read.ReadEvent
import net.techandgraphics.hymn.ui.theme.Typography

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
  state: SearchState,
  readEvent: (ReadEvent) -> Unit,
  event: (SearchEvent) -> Unit,
) {

  Column(
    modifier = Modifier.fillMaxSize()
  ) {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Quick Hymn Search",
      style = Typography.titleMedium,
      modifier = Modifier
        .padding(16.dp)
    )

    SearchBox(state, event)

    AnimatedVisibility(visible = state.searchQuery.isEmpty()) {
      Column {
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
          items(state.search, key = { it.query }) {
            ElevatedCard(
              modifier = Modifier
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(50))
                .combinedClickable(
                  onClick = { event(SearchEvent.SearchQueryTag(it.query)) },
                  onLongClick = { }
                ),
            ) {
              Text(
                text = "#${it.tag}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                maxLines = 1,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(8.dp)
              )
            }
          }
        }
      }
    }

    LazyColumn {
      itemsIndexed(state.lyrics, key = { _, item -> item.lyricId }) { index, item ->
        SearchScreenItem(
          lyric = item,
          readEvent = readEvent,
          index = index,
          size = state.lyrics.size,
          modifier = Modifier.animateItem()
        )
      }
    }
  }
}
