package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.techandgraphics.hymn.ui.screen.main.components.LyricScreenItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteDialog(
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
  onDismissRequest: () -> Unit,
) {
  ModalBottomSheet(onDismissRequest = onDismissRequest) {
    LazyColumn(state = rememberLazyListState()) {
      itemsIndexed(state.favorites, key = { _, lyric -> lyric.lyricId }) { index, lyric ->
        LyricScreenItem(
          lyric = lyric,
          modifier = Modifier.animateItem(),
          onEvent = onEvent,
          showDivider = index.plus(1) < state.favorites.size
        )
      }
    }
  }
}
