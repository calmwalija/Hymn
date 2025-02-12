package net.techandgraphics.hymn.ui.screen.theCategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.addRemoveFavoriteToast
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.screen.component.TimestampComponent

@Composable
fun CategorisationScreenItem(
  lyric: Lyric,
  onEvent: (TheCategoryUiEvent) -> Unit,
) {
  val context = LocalContext.current
  Row(
    modifier = Modifier
      .clickable { onEvent(TheCategoryUiEvent.ToPreview(lyric.number)) }
      .padding(8.dp)
      .fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = "#${lyric.number}",
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        color = MaterialTheme.colorScheme.primary
      )
      Text(
        text = lyric.content.trimIndent(),
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.bodyMedium
      )
      TimestampComponent(context, lyric)
    }

    IconButton(
      onClick = {
        context addRemoveFavoriteToast lyric
        onEvent(TheCategoryUiEvent.Favorite(lyric))
      }
    ) {
      Icon(
        imageVector = if (lyric.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
      )
    }

    Spacer(modifier = Modifier.width(8.dp))
  }
}
