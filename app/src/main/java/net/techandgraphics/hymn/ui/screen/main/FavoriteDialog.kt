package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toNumber
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent.Event
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent.OfType

@Composable
fun FavoriteDialog(
  favorites: List<Lyric>,
  onEvent: (MainUiEvent) -> Unit,
  onDismissRequest: () -> Unit,
) {
  Dialog(onDismissRequest = onDismissRequest) {
    ElevatedCard(modifier = Modifier.heightIn(max = 450.dp)) {
      LazyColumn(state = rememberLazyListState()) {
        itemsIndexed(favorites, key = { _, lyric -> lyric.lyricId }) { index, lyric ->
          Row(
            modifier = Modifier
              .animateItem()
              .clickable { onEvent(Event(OfType.Preview, lyric.number)) }
              .padding(8.dp)
              .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = lyric.toNumber(),
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.headlineSmall,
            )
            Column(
              modifier = Modifier
                .padding(16.dp)
                .weight(1f)
            ) {
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
          }
          if (index.plus(1) < favorites.size) HorizontalDivider()
        }
      }
    }
  }
}
