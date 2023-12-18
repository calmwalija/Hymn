package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

val searchFilters = listOf("Number", "Category", "Favorite")
val searchOrders = listOf("Ascending", "Descending")

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
    Row(
      modifier = Modifier
        .padding(end = 8.dp)
    ) {
      Text(
        text = "Quick Hymn Search",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .padding(16.dp)
          .weight(1f)
      )
    }

    ChatBoxScreen(state, event)

    AnimatedVisibility(visible = state.searchQuery.isEmpty()) {
      Column {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          text = "#search-tag-history",
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 8.dp)
        )

        LazyVerticalGrid(
          columns = GridCells.Fixed(3)
        ) {
          items(state.search, key = { it.query }) {
            ElevatedButton(
              onClick = { event(SearchEvent.SearchQueryTag(it.query)) },
              modifier = Modifier.padding(horizontal = 4.dp),
              shape = RoundedCornerShape(8),
            ) {
              Text(
                text = "#${it.tag}",
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }

    LazyColumn {
      items(state.lyric, key = { it.lyricId }) {
        SearchScreenItem(it, readEvent)
      }
    }
  }
}
