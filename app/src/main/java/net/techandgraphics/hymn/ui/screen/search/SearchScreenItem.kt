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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toTimeAgo

@Composable
fun SearchScreenItem(
  lyric: Lyric,
  modifier: Modifier = Modifier,
  onEvent: (Int) -> Unit,
) {

  val context = LocalContext.current

  Column(
    modifier = modifier
      .clickable { onEvent(lyric.number) }
      .fillMaxWidth(),
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {

      AsyncImage(
        model = Constant.images[lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .size(72.dp)
          .clip(RoundedCornerShape(50))
      )

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "#${lyric.number}",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.primary
        )
        Text(
          text = lyric.content.trimIndent(),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
        )
        Text(
          text = lyric.categoryName.trimIndent(),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.labelSmall,
        )

        Spacer(modifier = Modifier.height(2.dp))

        AnimatedVisibility(visible = lyric.timestamp != 0L) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              painter = painterResource(id = R.drawable.ic_access_time),
              contentDescription = null,
              modifier = Modifier.padding(end = 4.dp)
            )
            Text(
              text = lyric.timestamp.toTimeAgo(context),
              overflow = TextOverflow.Ellipsis,
              fontSize = MaterialTheme.typography.bodySmall.fontSize,
              letterSpacing = 0.sp
            )
          }
        }
      }
      Icon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = lyric.title,
        modifier = Modifier.padding(end = 16.dp),
        tint = MaterialTheme.colorScheme.primary
      )
    }
  }
}
