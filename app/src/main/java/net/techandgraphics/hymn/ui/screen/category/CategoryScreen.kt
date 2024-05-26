package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.theme.Typography

@Composable
fun CategoryScreen(
  state: CategoryState,
  event: (CategoryEvent) -> Unit,
) {
  Column {

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Quick Category Search",
      style = Typography.titleMedium,
      modifier = Modifier.padding(16.dp)
    )

    CategorySearchBox(state, event)

    LazyColumn {
      items(state.categories, key = { it.lyric.categoryId }) {
        CategoryScreenItem(
          category = it,
          event = event,
          modifier = Modifier.animateItem()
        )
      }
    }
  }
}
