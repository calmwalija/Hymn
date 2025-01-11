package net.techandgraphics.hymn.ui.screen.search.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun CategoryScreenItem(
  category: Category,
  event: (CategoryUiEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  Card(
    modifier = modifier
      .padding(8.dp)
      .size(160.dp),
    onClick = { }
  ) {
    Box {
      AsyncImage(
        model = Constant.images[category.lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        placeholder = painterResource(R.drawable.im_example)
      )
      Column(
        modifier = Modifier
          .background(color = MaterialTheme.colorScheme.surface.copy(alpha = .6f))
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
      ) {
        Text(
          text = category.lyric.categoryName,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = category.count.hymnCount(context),
          color = MaterialTheme.colorScheme.onSurface,
          style = MaterialTheme.typography.labelMedium
        )
      }
    }
  }
}

@PreviewLightDark
@Composable
fun CategoryScreenItemPreview() {
  HymnTheme {
    CategoryScreenItem(
      category = Faker.category,
      event = {}
    )
  }
}
