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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.Space

/**
 * Featured hymn card for the home rail. Text sits on a scrim over the artwork
 * so it stays legible whatever the image, rather than depending on the surface
 * colour fading in behind it — which inverted badly in dark mode.
 */
@Composable
fun UniquelyCraftedScreen(
  lyric: Lyric,
  onEvent: (MainUiEvent) -> Unit,
) {
  Surface(
    shape = MaterialTheme.shapes.large,
    tonalElevation = 1.dp,
    shadowElevation = 2.dp,
    onClick = {
      onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromUniquelyCrafted(lyric))
      onEvent(MainUiEvent.GotoPreview(lyric))
    },
    modifier = Modifier
      .width(260.dp)
      .height(160.dp),
  ) {
    Box {
      AsyncImage(
        model = Constant.imageFor(lyric.categoryId),
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
              0.45f to Color.Black.copy(alpha = 0.35f),
              1f to Color.Black.copy(alpha = 0.82f),
            ),
          ),
      )

      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .fillMaxWidth()
          .padding(Space.sm),
      ) {
        Text(
          text = "#${lyric.number}",
          style = MaterialTheme.typography.labelMedium,
          color = Color.White.copy(alpha = 0.85f),
        )
        Text(
          text = lyric.title,
          style = MaterialTheme.typography.titleMedium,
          color = Color.White,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = lyric.categoryName,
          style = MaterialTheme.typography.bodySmall,
          color = Color.White.copy(alpha = 0.8f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
    }
  }
}

@PreviewLightDark
@Composable
private fun UniquelyCraftedScreenPreview() {
  HymnTheme {
    UniquelyCraftedScreen(lyric = Faker.lyric) {}
  }
}
