package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.diveInto
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun DiveIntoItemScreen(
  data: Lyric,
  event: (PreviewUiEvent) -> Unit,
) {
  val context = LocalContext.current
  val visibility = if (data.timestamp != 0L) 1f else 0f

  ElevatedCard(
    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
    shape = RoundedCornerShape(50)
  ) {

    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
      AsyncImage(
        model = Constant.images[data.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .clip(RoundedCornerShape(50))
          .size(62.dp),
        placeholder = painterResource(id = R.drawable.im_help)
      )

      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
      ) {

        Text(
          text = "#${data.number}",
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.bodyLarge,
          color = MaterialTheme.colorScheme.primary,
        )

        Text(
          text = data.content.replace("\n", ""),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
          modifier = Modifier
            .padding(vertical = 2.dp)
        )

        Text(
          text = data.categoryName,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.primary,
        )
      }

      Text(
        text = data.timestamp.toTimeAgo(context).diveInto(),
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
          .alpha(visibility)
          .padding(end = 16.dp)
      )
    }
  }
}

@Composable
@Preview(showBackground = true)
fun DiveIntoItemScreenPreview() {
  HymnTheme {
    DiveIntoItemScreen(Faker.lyric) {
    }
  }
}
