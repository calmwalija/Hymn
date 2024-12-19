package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.screen.search.lyric.LyricScreenItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDialog(
  show: MutableState<Boolean>,
  state: MiscState,
  event: (MiscEvent) -> Unit,
  onEvent: (MainEvent) -> Unit
) {

  if (show.value && state.favorites.isNotEmpty()) {

    Dialog(onDismissRequest = { show.value = false }) {

      Card(
        colors = CardDefaults.elevatedCardColors(),
        elevation = CardDefaults.elevatedCardElevation(
          defaultElevation = 2.dp
        ),
        modifier = Modifier.heightIn(max = 405.dp)
      ) {
        LazyColumn(
          state = rememberLazyListState()
        ) {
          itemsIndexed(
            state.favorites,
            key = { _, lyric -> lyric.lyricId }
          ) { index, lyric ->
            val dismissState = rememberSwipeToDismissBoxState(
              confirmValueChange = {
                if (it == SwipeToDismissBoxValue.StartToEnd)
                  event(MiscEvent.RemoveFav(lyric))
                true
              },
              positionalThreshold = { 150f },
            )
            SwipeToDismissBox(
              state = dismissState,
              enableDismissFromStartToEnd = true,
              enableDismissFromEndToStart = false,
              backgroundContent = {
                Row(
                  modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                  Text(text = "Delete", style = MaterialTheme.typography.labelMedium)
                }
              },
              content = {
                LyricScreenItem(
                  lyric = lyric,
                  //                  index = index,
//                  size = state.favorites.size,
                  modifier = Modifier.animateItem()
                ) {}
              }
            )
          }
        }
      }
    }
  }
}
