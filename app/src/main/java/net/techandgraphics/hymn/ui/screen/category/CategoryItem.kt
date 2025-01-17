package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent

@Composable
fun CategoryItem(
  category: Category,
  onEvent: (MainUiEvent) -> Unit,
) {
  val context = LocalContext.current
  Column(
    horizontalAlignment = Alignment.End,
    modifier = Modifier.clickable {
      onEvent(MainUiEvent.CategoryUiEvent.GoTo(category))
    }
  ) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)) {
      AsyncImage(
        model = Constant.images[category.lyric.categoryId].drawableRes,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
          .clip(RoundedCornerShape(24))
          .size(62.dp),
        placeholder = painterResource(R.drawable.im_example)
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = 16.dp),
      ) {
        Text(
          text = category.lyric.categoryName,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = category.count.hymnCount(context),
          style = MaterialTheme.typography.labelMedium
        )
      }

      Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        modifier = Modifier
          .size(24.dp)
          .padding(4.dp),
        tint = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.width(8.dp))
    }

    HorizontalDivider(
      modifier = Modifier
        .padding(end = 32.dp)
        .fillMaxSize(.74f)
    )
  }
}

// @PreviewLightDark
// @Composable
// fun CategoryItemPreview() {
//  HymnTheme {
//    CategoryItem(
//      category = Faker.category,
//      event = {}
//    )
//  }
// }
