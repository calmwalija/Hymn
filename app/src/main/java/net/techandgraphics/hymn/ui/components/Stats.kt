package net.techandgraphics.hymn.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Space

/** A single headline number with its label — the building block of the stats grid. */
@Composable
fun StatTile(
  value: String,
  label: String,
  icon: ImageVector,
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surfaceContainer,
    modifier = modifier,
  ) {
    Column(modifier = Modifier.padding(Space.md)) {
      androidx.compose.material3.Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(20.dp),
      )
      Spacer(Modifier.height(Space.xs))
      Text(
        text = value,
        style = MaterialTheme.typography.headlineSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
      Text(
        text = label,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

/**
 * A ranked row with a proportional bar behind it. Turns the old plain
 * "#12 Title / 4 opens" text pairs into something you can actually compare
 * at a glance.
 */
@Composable
fun RankedBarRow(
  rank: Int,
  title: String,
  supporting: String,
  metric: String,
  fraction: Float,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val animated by animateFloatAsState(
    targetValue = fraction.coerceIn(0f, 1f),
    label = "rankedBar",
  )
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(MaterialTheme.shapes.medium)
      .clickable(onClick = onClick),
  ) {
    // Proportional fill sits behind the content rather than beside it, so the
    // row stays readable at any width.
    Box(
      modifier = Modifier
        .fillMaxWidth(animated)
        .height(56.dp)
        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f)),
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
        .padding(horizontal = Space.sm),
    ) {
      Text(
        text = rank.toString(),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(end = Space.sm),
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.titleSmall,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = supporting,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Text(
        text = metric,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  }
}

/** Read-only pill used for themes and language splits. */
@Composable
fun InfoPill(
  text: String,
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = PillShape,
    color = MaterialTheme.colorScheme.secondaryContainer,
    modifier = modifier,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      modifier = Modifier.padding(horizontal = Space.sm, vertical = Space.xs),
    )
  }
}
