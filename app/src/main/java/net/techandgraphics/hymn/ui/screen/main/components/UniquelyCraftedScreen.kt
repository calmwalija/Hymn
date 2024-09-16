package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.main.MainEvent

@Composable
fun UniquelyCraftedScreen(
  lyric: Lyric,
  onEvent: (MainEvent) -> Unit,
) {
  val context = LocalContext.current
  Box(
    modifier = Modifier
      .padding(8.dp)
      .width(200.dp)
      .height(130.dp)
      .clip(RoundedCornerShape(8))
      .clickable { onEvent(MainEvent.Event(MainEvent.OfType.Read, lyric.number)) },
  ) {

    AsyncImage(
      model = Constant.images[lyric.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier.fillMaxSize(),
      placeholder = painterResource(R.drawable.im_help),
    )

    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
      ),
      shape = RoundedCornerShape(topStart = 12.dp),
      modifier = Modifier
        .padding(top = 8.dp)
        .fillMaxWidth(0.85f)
        .align(Alignment.BottomEnd)
    ) {
      Column(
        modifier = Modifier.padding(16.dp)
      ) {
        Text(
          text = "#${lyric.number}",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = lyric.categoryName,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall,
          textDecoration = TextDecoration.Underline,
          modifier = Modifier.padding(vertical = 2.dp),
        )
        Text(
          text = lyric.content,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
        )
        AnimatedVisibility(visible = lyric.timestamp != 0L) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(top = 2.dp),
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_access_time),
              contentDescription = null,
              modifier = Modifier.padding(end = 4.dp),
            )
            Text(
              text = lyric.timestamp.toTimeAgo(context),
              overflow = TextOverflow.Ellipsis,
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
fun UniquelyCraftedScreenPreview() {
  UniquelyCraftedScreen(lyric = Faker.lyric) {
  }
}
