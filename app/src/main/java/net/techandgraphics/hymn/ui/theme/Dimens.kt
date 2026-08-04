package net.techandgraphics.hymn.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale. One 4dp-based ramp for every gap in the app so screens line up
 * with each other instead of each picking its own 8/12/14/20dp.
 */
object Space {
  /** 4dp — hairline gaps inside a single component. */
  val xxs = 4.dp

  /** 8dp — between tightly related elements. */
  val xs = 8.dp

  /** 12dp — inner card padding, gaps in a row of controls. */
  val sm = 12.dp

  /** 16dp — the default screen gutter and list-item padding. */
  val md = 16.dp

  /** 24dp — between sections. */
  val lg = 24.dp

  /** 32dp — around hero/empty-state content. */
  val xl = 32.dp

  /** 48dp — large vertical breathing room. */
  val xxl = 48.dp
}

object Sizes {
  /** Minimum interactive target, per the Material accessibility guidance. */
  val minTouchTarget = 48.dp

  /** Leading artwork on a hymn row. */
  val listArt = 56.dp

  /** Circular hymn-number badge. */
  val numberBadge = 44.dp

  /** Icon inside a list row or app-bar action. */
  val icon = 24.dp

  /** Height of the floating bottom navigation capsule. */
  val navBar = 64.dp

  /** Category artwork header on the category detail screen. */
  val categoryHeader = 220.dp
}
