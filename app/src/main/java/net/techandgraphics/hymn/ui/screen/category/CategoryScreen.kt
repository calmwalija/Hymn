package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CategoryScreen(
  state: CategoryState,
  event: (CategoryEvent) -> Unit
) {
  Column {
    Row(
      modifier = Modifier.padding(horizontal = 6.dp)
    ) {
      Text(
        text = "Browse Hymn By Category",
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 32.dp, bottom = 8.dp)
      )
    }
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
      items(state.categories, key = { it.lyric.categoryId }) {
        CategoryScreenItem(it, event)
      }
    }
  }
}
