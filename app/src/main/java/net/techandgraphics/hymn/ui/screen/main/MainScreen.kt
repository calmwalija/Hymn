package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent
import net.techandgraphics.hymn.ui.screen.category.CategoryScreenItem
import net.techandgraphics.hymn.ui.screen.main.components.HymnItemScreen
import net.techandgraphics.hymn.ui.screen.main.components.HymnOfTheDayScreen
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@Composable
fun MainScreen(
  state: MainState,
  categoryEvent: (CategoryEvent) -> Unit,
  readEvent: (ReadEvent) -> Unit,
) {

  LazyColumn {

    item {

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "Uniquely Crafted",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
      )

      if (state.ofTheDay.isNotEmpty())
        HymnOfTheDayScreen(state.ofTheDay.first())
    }

    item {

      Text(
        text = "Quick Access",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
      )

      LazyRow {
        items(state.theHymn, key = { it.lyricId }) {
          HymnItemScreen(it, readEvent)
        }
      }
    }

    item {

      Text(
        text = "Featured Category",
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
      )

      LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(300.dp)
      ) {
        items(state.featured, key = { it.lyric.categoryId }) {
          CategoryScreenItem(it, categoryEvent)
        }
      }
    }
  }
}
