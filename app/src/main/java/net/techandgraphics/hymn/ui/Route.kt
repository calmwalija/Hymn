package net.techandgraphics.hymn.ui

import kotlinx.serialization.Serializable

sealed interface Route {
  @Serializable data object Home : Route
  @Serializable data object Browse : Route
  @Serializable data object Insights : Route
  @Serializable data object Library : Route
  @Serializable data object Favorites : Route
  @Serializable data object History : Route
  @Serializable data object Settings : Route
  @Serializable data class YearInHymns(val year: Int) : Route
  @Serializable data class Preview(val id: Int) : Route
  @Serializable data class TheCategory(val id: Int) : Route
}
