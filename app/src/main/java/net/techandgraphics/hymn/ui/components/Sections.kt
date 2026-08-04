package net.techandgraphics.hymn.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

/**
 * Section heading for a group of content, with an optional "see all" action.
 * Keeps every rail and list on the same baseline and gutter.
 */
@Composable
fun SectionHeader(
  title: String,
  modifier: Modifier = Modifier,
  actionLabel: String? = null,
  onAction: (() -> Unit)? = null,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.weight(1f),
    )
    if (actionLabel != null && onAction != null) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(MaterialTheme.shapes.small)
          .clickable(onClick = onAction)
          .padding(horizontal = Space.xs, vertical = Space.xxs),
      ) {
        Text(
          text = actionLabel,
          style = MaterialTheme.typography.labelLarge,
          color = MaterialTheme.colorScheme.primary,
        )
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp),
        )
      }
    }
  }
}

/**
 * The one empty state in the app: icon, headline, explanation, optional action.
 * Screens previously each rendered a bare sentence in a different place.
 */
@Composable
fun EmptyState(
  icon: ImageVector,
  title: String,
  message: String,
  modifier: Modifier = Modifier,
  action: (@Composable () -> Unit)? = null,
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(Space.xl),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(56.dp),
    )
    Spacer(Modifier.height(Space.md))
    Text(
      text = title,
      style = MaterialTheme.typography.titleLarge,
      textAlign = TextAlign.Center,
    )
    Spacer(Modifier.height(Space.xs))
    Text(
      text = message,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
    if (action != null) {
      Spacer(Modifier.height(Space.lg))
      action()
    }
  }
}

/** Navigation row used by the Library hub and Settings groups. */
@Composable
fun NavigationRow(
  title: String,
  subtitle: String?,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  trailing: (@Composable () -> Unit)? = null,
) = NavigationRow(
  title = title,
  subtitle = subtitle,
  onClick = onClick,
  modifier = modifier,
  trailing = trailing,
  leading = {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.primary,
      modifier = Modifier.size(Sizes.icon),
    )
  },
)

/** Leading-slot variant, for rows whose icon is a drawable rather than a vector asset. */
@Composable
fun NavigationRow(
  title: String,
  subtitle: String?,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  trailing: (@Composable () -> Unit)? = null,
  leading: (@Composable () -> Unit)? = null,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .heightIn(min = Sizes.minTouchTarget)
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    leading?.invoke()
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = if (leading != null) Space.md else 0.dp),
    ) {
      Text(text = title, style = MaterialTheme.typography.titleMedium)
      if (subtitle != null) {
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    trailing?.invoke() ?: Icon(
      imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
