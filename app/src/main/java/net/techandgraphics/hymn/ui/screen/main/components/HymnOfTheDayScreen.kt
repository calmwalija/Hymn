package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.main.MainEvent
import net.techandgraphics.hymn.ui.screen.read.ReadEvent
import kotlin.random.Random

@Composable
fun HymnOfTheDayScreen(
  data: Lyric,
  event: (MainEvent) -> Unit,
  readEvent: (ReadEvent) -> Unit,
) {
  val context = LocalContext.current
  val ofTheDay by remember {
    mutableStateOf(Constant.ofTheDay[Random.nextInt(Constant.ofTheDay.size)])
  }

  Box(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
      .clip(RoundedCornerShape(8))
      .clickable { readEvent(ReadEvent.Click(data.number)) },
  ) {

    AsyncImage(
      model = ofTheDay,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
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
      Row(
        modifier = Modifier.padding(16.dp)
      ) {
        Column(
          modifier = Modifier.weight(1f)
        ) {
          Text(
            text = "#${data.number}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
          )
          Text(
            text = data.categoryName,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.padding(vertical = 2.dp),
          )
          Text(
            text = data.content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
              .fillMaxWidth()
              .padding(end = 4.dp),
          )
          AnimatedVisibility(visible = data.timestamp != 0L) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .padding(top = 4.dp),
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
        IconButton(
          onClick = { event(MainEvent.Favorite(data)) },
          modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surface)
            .align(Alignment.Bottom)
        ) {
          Icon(
            imageVector = if (data.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
          )
        }
      }
    }
  }
}
