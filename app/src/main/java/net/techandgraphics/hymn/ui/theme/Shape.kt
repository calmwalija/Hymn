package net.techandgraphics.hymn.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * The Material 3 shape scale. Components should pull corners from here
 * (`MaterialTheme.shapes.large`) rather than declaring one-off radii, which is
 * what produced the earlier mix of `RoundedCornerShape(20)` — a *percentage* —
 * next to `RoundedCornerShape(20.dp)`.
 */
val HymnShapes = Shapes(
  extraSmall = RoundedCornerShape(4.dp),
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(12.dp),
  large = RoundedCornerShape(16.dp),
  extraLarge = RoundedCornerShape(28.dp),
)

/** Fully rounded — chips, pills, search fields, the floating nav capsule. */
val PillShape = RoundedCornerShape(percent = 50)
