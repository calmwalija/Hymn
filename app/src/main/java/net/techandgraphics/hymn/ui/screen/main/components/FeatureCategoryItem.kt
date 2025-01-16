package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun FeaturedCategoryItem(
  lyric: Lyric,
  onEvent: (MainUiEvent) -> Unit,
) {
  Card(
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .width(180.dp)
      .height(80.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp,
    ),
    shape = RoundedCornerShape(4),
    onClick = {
      onEvent(MainUiEvent.Event(MainUiEvent.OfType.Category, lyric.categoryId))
    }
  ) {
    Box {
      AsyncImage(
        model = Constant.images[lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        placeholder = painterResource(R.drawable.im_coming_again),
      )

      Box(
        modifier = Modifier
          .background(color = MaterialTheme.colorScheme.surface.copy(alpha = .8f))
          .fillMaxSize(),
        contentAlignment = Alignment.Center
      ) {
        Text(
          textAlign = TextAlign.Center,
          text = lyric.categoryName,
          maxLines = 3,
          modifier = Modifier.padding(8.dp),
          color = MaterialTheme.colorScheme.onSurface,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SpotlightItemPreview() {
  HymnTheme {
    FeaturedCategoryItem(Faker.lyric) {
    }
  }
}
