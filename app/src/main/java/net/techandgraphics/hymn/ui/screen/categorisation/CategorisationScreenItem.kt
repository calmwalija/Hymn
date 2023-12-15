package net.techandgraphics.hymn.ui.screen.categorisation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@Composable
fun CategorisationScreenItem(
  data: LyricEntity = Faker.lyricEntity,
  readEvent: (ReadEvent) -> Unit
) {

  val context = LocalContext.current

  Row(
    modifier = Modifier
      .clickable { readEvent(ReadEvent.Click(data.number)) }
      .padding(8.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {

    Column(
      modifier = Modifier.weight(1f)
    ) {

      Text(
        text = "#${data.number}",
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = MaterialTheme.colorScheme.primary
      )

      Text(
        text = data.content.trimIndent(),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
      )

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
      imageVector = Icons.Default.FavoriteBorder,
      contentDescription = null,
      modifier = Modifier
        .size(32.dp)
        .padding(end = 12.dp),
      tint = MaterialTheme.colorScheme.primary
    )

    Spacer(modifier = Modifier.width(8.dp))
  }
}
