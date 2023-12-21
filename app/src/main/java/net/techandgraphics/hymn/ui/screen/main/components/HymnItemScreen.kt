package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@Composable
fun HymnItemScreen(
  data: LyricEntity = Faker.lyricEntity,
  event: (ReadEvent) -> Unit
) {
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  val widthInDp = (configuration.screenWidthDp.dp / 2) - 8.dp

  Box(
    modifier = Modifier
      .padding(vertical = 4.dp)
      .width(widthInDp)
      .height(IntrinsicSize.Max)
      .clip(RoundedCornerShape(8))
      .clickable { event(ReadEvent.Click(data.number)) },
  ) {

    AsyncImage(
      model = Constant.images[data.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop
    )

    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
      ),
      shape = RoundedCornerShape(topStart = 12.dp),
      modifier = Modifier
        .padding(top = 24.dp)
        .fillMaxWidth(0.85f)
        .align(Alignment.BottomEnd)
    ) {
      Column(
        modifier = Modifier
          .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.Center
      ) {

        Text(
          text = "#${data.number}",
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyLarge,
        )
        Text(
          text = data.categoryName,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall,
          textDecoration = TextDecoration.Underline,
        )
        Text(
          text = data.content.replace("\n", ""),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier
            .padding(vertical = 2.dp)
        )

        val visibility = if (data.timestamp != 0L) 1f else 0f

        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .alpha(visibility)
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_access_time),
            contentDescription = null,
            modifier = Modifier.padding(end = 4.dp),
            tint = MaterialTheme.colorScheme.primary
          )
          Text(
            text = data.timestamp.toTimeAgo(context),
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
          )
        }
      }
    }
  }
}
