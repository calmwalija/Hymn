package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.Space

/**
 * Theme card for the home rail and the themes grid. Shares the scrim treatment
 * with the featured hymn card so both rails read as one family.
 */
@Composable
fun FeaturedCategoryItem(
  category: Category,
  onEvent: (MainUiEvent) -> Unit,
  modifier: Modifier = Modifier.width(160.dp).height(112.dp),
) {
  val context = LocalContext.current
  Surface(
    shape = MaterialTheme.shapes.large,
    tonalElevation = 1.dp,
    shadowElevation = 2.dp,
    onClick = {
      onEvent(MainUiEvent.AnalyticEvent.GotoTheCategory(category))
      onEvent(MainUiEvent.GotoCategory(category))
    },
    modifier = modifier,
  ) {
    Box {
      AsyncImage(
        model = Constant.imageFor(category.lyric.categoryId),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize(),
        placeholder = painterResource(R.drawable.im_coming_again),
      )
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              0f to Color.Transparent,
              1f to Color.Black.copy(alpha = 0.78f),
            ),
          ),
      )
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(Space.xs),
      ) {
        Text(
          text = category.lyric.categoryName,
          style = MaterialTheme.typography.titleSmall,
          color = Color.White,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = category.count.hymnCount(context),
          style = MaterialTheme.typography.bodySmall,
          color = Color.White.copy(alpha = 0.8f),
          maxLines = 1,
        )
      }
    }
  }
}

/** Grid variant: fills its column and stands a little taller. */
@Composable
fun CategoryGridItem(
  category: Category,
  onEvent: (MainUiEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  FeaturedCategoryItem(
    category = category,
    onEvent = onEvent,
    modifier = modifier
      .fillMaxWidth()
      .height(124.dp),
  )
}

@PreviewLightDark
@Composable
private fun FeaturedCategoryItemPreview() {
  HymnTheme {
    FeaturedCategoryItem(category = Category(Faker.lyric, "3"), onEvent = {})
  }
}
