package net.techandgraphics.hymn.ui.screen.search.lyric

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.removeNextLine
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun LyricScreenItem(
  lyric: Lyric,
  modifier: Modifier = Modifier,
  onEvent: (Int) -> Unit,
) {

  val context = LocalContext.current

  Column(
    modifier = modifier
      .padding(vertical = 8.dp)
      .clickable { onEvent(lyric.number) }
      .fillMaxWidth(),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
      AsyncImage(
        model = Constant.images[lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .padding(horizontal = 8.dp)
          .size(80.dp)
          .clip(RoundedCornerShape(24)),
        placeholder = painterResource(id = R.drawable.im_example)
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = "#${lyric.number}",
          letterSpacing = 0.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = lyric.content.removeNextLine(),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
        )
        Text(
          text = lyric.categoryName,
          maxLines = 1,
          color = MaterialTheme.colorScheme.primary,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.labelMedium,
          letterSpacing = 0.sp
        )
        AnimatedVisibility(visible = lyric.timestamp == 0L) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              painter = painterResource(id = R.drawable.ic_access_time),
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
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
      Spacer(modifier = Modifier.width(24.dp))
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SearchScreenItemPreview() {
  HymnTheme {
    LyricScreenItem(
      lyric = Faker.lyric,
      modifier = Modifier
    ) {
    }
  }
}
