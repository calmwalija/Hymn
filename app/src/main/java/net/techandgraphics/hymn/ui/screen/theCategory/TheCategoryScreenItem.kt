package net.techandgraphics.hymn.ui.screen.theCategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.addRemoveFavoriteToast
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.components.NumberBadge
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

@Composable
fun CategorisationScreenItem(
  lyric: Lyric,
  onEvent: (TheCategoryUiEvent) -> Unit,
) {
  val context = LocalContext.current
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onEvent(TheCategoryUiEvent.ToPreview(lyric.number)) }
      .heightIn(min = Sizes.minTouchTarget)
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    NumberBadge(number = lyric.number)
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(horizontal = Space.sm),
    ) {
      Text(
        text = lyric.title,
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = lyric.content.trimIndent(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
      )
    }
    IconButton(
      onClick = {
        context addRemoveFavoriteToast lyric
        onEvent(TheCategoryUiEvent.Favorite(lyric))
      },
    ) {
      Icon(
        imageVector = if (lyric.favorite) Icons.Rounded.Favorite
        else Icons.Rounded.FavoriteBorder,
        contentDescription = stringResource(
          if (lyric.favorite) R.string.action_remove_favorite
          else R.string.action_add_favorite,
        ),
        tint = if (lyric.favorite) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}
