package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.data.local.join.Category
import net.techandgraphics.hymn.hymnCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenItem(
  category: Category,
  event: (CategoryEvent) -> Unit,
) {
  val context = LocalContext.current
  val configuration = LocalConfiguration.current
  val widthInDp = (configuration.screenWidthDp.dp / 2) - 8.dp
  Box(
    modifier = Modifier
      .padding(vertical = 4.dp)
      .width(widthInDp)
      .height(IntrinsicSize.Max)
      .clip(shape = RoundedCornerShape(6))
      .clickable { event.invoke(CategoryEvent.Click(category.lyric.categoryId)) }
  ) {
    AsyncImage(
      model = Constant.images[category.lyric.categoryId].drawableRes,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
    )
    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
      ),
      shape = RoundedCornerShape(topStart = 12.dp),
      modifier = Modifier
        .fillMaxWidth(0.85f)
        .align(Alignment.BottomEnd)
    ) {
      Column(
        modifier = Modifier
          .padding(12.dp)
          .wrapContentHeight()
      ) {
        Text(
          text = category.lyric.categoryName,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          lineHeight = 17.sp,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Badge(
          containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
          Text(
            text = category.count.hymnCount(context),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
          )
        }
      }
    }
  }
}
