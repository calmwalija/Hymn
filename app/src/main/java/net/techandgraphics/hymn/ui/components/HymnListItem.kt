package net.techandgraphics.hymn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

/**
 * The canonical hymn row. Every list of hymns in the app — search results,
 * browse, favorites, history, category detail — renders through this so the
 * artwork, number, title and favourite affordance never drift apart.
 */
@Composable
fun HymnListItem(
  lyric: Lyric,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  supporting: String? = null,
  showArtwork: Boolean = true,
  onFavorite: (() -> Unit)? = null,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .heightIn(min = Sizes.minTouchTarget)
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    if (showArtwork) {
      CategoryArtwork(
        categoryId = lyric.categoryId,
        modifier = Modifier.size(Sizes.listArt),
      )
    } else {
      NumberBadge(number = lyric.number)
    }

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
        text = supporting ?: "#${lyric.number} · ${lyric.categoryName}",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }

    if (onFavorite != null) {
      IconButton(onClick = onFavorite) {
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
}

/** Category artwork, rounded and bounds-safe. */
@Composable
fun CategoryArtwork(
  categoryId: Int,
  modifier: Modifier = Modifier,
) {
  AsyncImage(
    model = Constant.imageFor(categoryId),
    contentDescription = null,
    contentScale = ContentScale.Crop,
    modifier = modifier.clip(MaterialTheme.shapes.medium),
  )
}

/** Circular hymn number, used where artwork would be too heavy. */
@Composable
fun NumberBadge(
  number: Int,
  modifier: Modifier = Modifier,
) {
  Box(
    modifier = modifier
      .size(Sizes.numberBadge)
      .clip(MaterialTheme.shapes.medium)
      .background(MaterialTheme.colorScheme.primaryContainer),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = number.toString(),
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
    )
  }
}

@PreviewLightDark
@Composable
private fun HymnListItemPreview() {
  HymnTheme {
    Column {
      HymnListItem(lyric = Faker.lyric, onClick = {}, onFavorite = {})
      HymnListItem(lyric = Faker.lyric, onClick = {}, showArtwork = false)
    }
  }
}
