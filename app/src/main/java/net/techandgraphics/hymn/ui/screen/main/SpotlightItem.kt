package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent

@Composable
fun SpotlightItem(
  lyric: Lyric,
  event: (CategoryEvent) -> Unit,
) {

  Card(
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surfaceContainer
    ),
    elevation = CardDefaults.elevatedCardElevation(
      defaultElevation = 1.dp,
    ),
    modifier = Modifier
      .height(IntrinsicSize.Max)
      .padding(bottom = 16.dp)
      .padding(horizontal = 8.dp),
    shape = RoundedCornerShape(8)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clickable { event.invoke(CategoryEvent.Click(lyric.categoryId)) },
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage(
        model = Constant.images[lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .fillMaxSize()
          .weight(.35f)
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(vertical = 32.dp, horizontal = 12.dp)
      ) {
        Text(
          text = lyric.categoryName,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
        contentDescription = null,
        modifier = Modifier.size(22.dp),
        tint = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.width(2.dp))
    }
  }
}
