package net.techandgraphics.hymn.ui.screen.search.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.hymnCount

@Composable
fun CategoryScreenItem(
  category: Category,
  event: (CategoryUiEvent) -> Unit,
  modifier: Modifier = Modifier,
) {
  val context = LocalContext.current
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .clickable { event.invoke(CategoryUiEvent.Click(category.lyric.categoryId)) }
      .padding(8.dp)
  ) {
    AsyncImage(
      model = Constant.images[category.lyric.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .clip(RoundedCornerShape(20))
        .size(62.dp)
    )
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = 8.dp)
    ) {
      Text(
        text = category.lyric.categoryName,
      )
      Text(
        text = category.count.hymnCount(context),
        style = MaterialTheme.typography.labelMedium
      )
    }

    Icon(
      imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
      contentDescription = null,
      modifier = Modifier
        .padding(end = 2.dp)
        .size(18.dp),
      tint = MaterialTheme.colorScheme.primary
    )
  }
}
