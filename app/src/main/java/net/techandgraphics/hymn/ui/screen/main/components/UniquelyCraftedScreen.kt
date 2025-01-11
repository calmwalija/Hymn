package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.component.TimestampComponent
import net.techandgraphics.hymn.ui.screen.component.TimestampFormat
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun UniquelyCraftedScreen(
  lyric: Lyric,
  onEvent: (MainEvent) -> Unit,
) {
  val context = LocalContext.current
  Card(
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .width(320.dp)
      .height(180.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp,
    ),
    colors = CardDefaults.cardColors(),
    onClick = {
      onEvent(MainEvent.Event(MainEvent.OfType.Read, lyric.number))
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

      Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxSize()
      ) {

        Row(
          horizontalArrangement = Arrangement.End,
          modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
        ) {

          Card(shape = RoundedCornerShape(8)) {
            Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
              TimestampComponent(context, lyric, TimestampFormat.Short)
            }
          }
        }

        Column(
          modifier = Modifier
            .background(
              brush = Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                  MaterialTheme.colorScheme.surface.copy(alpha = .8f),
                )
              )
            )
            .fillMaxWidth()
            .padding(16.dp)
        ) {

          Text(
            text = "#${lyric.number}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
          )

          Text(
            text = lyric.title,
            maxLines = 1,
            color = MaterialTheme.colorScheme.primary,
            overflow = TextOverflow.Ellipsis,
          )

          Text(
            text = lyric.categoryName,
            maxLines = 3,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
          )
        }
      }
    }
  }
}

@Composable
@PreviewLightDark
fun UniquelyCraftedScreenPreview() {
  HymnTheme {
    UniquelyCraftedScreen(lyric = Faker.lyric) {
    }
  }
}
