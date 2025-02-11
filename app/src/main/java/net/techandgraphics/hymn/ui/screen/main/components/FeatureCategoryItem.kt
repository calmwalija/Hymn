package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun FeaturedCategoryItem(
  category: Category,
  onEvent: (MainUiEvent) -> Unit,
) {
  Card(
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .width(200.dp)
      .height(120.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp,
    ),
    shape = RoundedCornerShape(4),
    onClick = {
      onEvent(MainUiEvent.Event(MainUiEvent.OfType.Category, category.lyric.categoryId))
    }
  ) {

    Box(contentAlignment = Alignment.Center) {
      AsyncImage(
        model = Constant.images[category.lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        placeholder = painterResource(R.drawable.im_coming_again),
      )
      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        ),
        shape = RoundedCornerShape(topStart = 16.dp),
        modifier = Modifier
          .fillMaxWidth(0.75f)
          .align(Alignment.BottomEnd)
      ) {
        Column(
          modifier = Modifier
            .padding(16.dp)
            .wrapContentHeight()
        ) {
          Text(
            text = category.lyric.categoryName,
            maxLines = 1,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SpotlightItemPreview() {
  HymnTheme {
    FeaturedCategoryItem(Category(Faker.lyric, "3")) {
    }
  }
}
