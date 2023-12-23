package net.techandgraphics.hymn.ui.screen.categorisation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorisationScreen(
  state: CategorisationState,
  readEvent: (ReadEvent) -> Unit,
  event: (CategorisationEvent) -> Unit,
) {

  val context = LocalContext.current

  Column {

    if (state.category.isNotEmpty()) {
      val data = state.category.first()
      Box(
        contentAlignment = Alignment.Center
      ) {

        AsyncImage(
          model = Constant.images[data.lyric.categoryId].drawableRes,
          contentDescription = null,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .shadow(elevation = 10.dp)
        )

        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
          ),
          shape = RoundedCornerShape(topStart = 12.dp),
          modifier = Modifier
            .fillMaxWidth(0.75f)
            .align(Alignment.BottomEnd)
        ) {
          Column(
            modifier = Modifier
              .padding(12.dp)
              .wrapContentHeight()
          ) {
            Text(
              text = data.lyric.categoryName,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Spacer(modifier = Modifier.height(4.dp))
            Badge(
              containerColor = MaterialTheme.colorScheme.primary,
            ) {
              Text(
                text = data.count.hymnCount(context),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
              )
            }
          }
        }
      }
    }

    LazyColumn {
      items(state.lyric, key = { it.lyricId }) {
        CategorisationScreenItem(it, readEvent, event)
      }
    }
  }
}
