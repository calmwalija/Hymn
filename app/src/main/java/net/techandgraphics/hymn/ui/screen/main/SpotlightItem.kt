package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun SpotlightItem(
  lyric: Lyric,
  onEvent: (MainEvent) -> Unit,
) {

  Column(
    modifier = Modifier
      .padding(2.dp)
      .clip(RoundedCornerShape(8))
      .width(140.dp)
      .clickable { onEvent(MainEvent.Event(MainEvent.OfType.Category, lyric.categoryId)) }
      .padding(2.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Spacer(modifier = Modifier.height(2.dp))

    AsyncImage(
      model = Constant.images[lyric.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      placeholder = painterResource(id = R.drawable.im_example),
      modifier = Modifier
        .clip(RoundedCornerShape(20))
        .width(120.dp)
        .height(90.dp)
    )
    Text(
      text = lyric.categoryName,
      maxLines = 2,
      textAlign = TextAlign.Center,
      style = MaterialTheme.typography.labelLarge,
      modifier = Modifier.padding(4.dp),
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Preview(showBackground = true)
@Composable
fun SpotlightItemPreview() {
  HymnTheme {
    SpotlightItem(Faker.lyric) {
    }
  }
}
