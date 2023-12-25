package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@Composable
fun SearchScreenItem(
  data: Lyric,
  readEvent: (ReadEvent) -> Unit
) {

  val context = LocalContext.current

  Row(
    modifier = Modifier
      .clickable { readEvent(ReadEvent.Click(data.number)) }
      .padding(vertical = 8.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {

    AsyncImage(
      model = Constant.images[data.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .padding(horizontal = 8.dp)
        .size(64.dp)
        .clip(RoundedCornerShape(16))
    )

    Column(
      modifier = Modifier.weight(1f)
    ) {

      Text(
        text = "#${data.number}",
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.primary
      )

      Row {
        Text(
          text = data.content.trimIndent(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
        )
      }

      Text(
        text = data.categoryName.trimIndent(),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall,
        textDecoration = TextDecoration.Underline,
      )

      Spacer(modifier = Modifier.height(2.dp))
      AnimatedVisibility(visible = data.timestamp != 0L) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_access_time),
            contentDescription = null,
            modifier = Modifier.padding(end = 4.dp)
          )
          Text(
            text = data.timestamp.toTimeAgo(context),
            overflow = TextOverflow.Ellipsis,
            fontSize = MaterialTheme.typography.bodySmall.fontSize,
            letterSpacing = 0.sp
          )
        }
      }
    }

    Icon(
      imageVector = Icons.Filled.KeyboardArrowRight,
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .padding(end = 12.dp),
      tint = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.width(8.dp))
  }
}
