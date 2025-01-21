package net.techandgraphics.hymn.ui

import kotlinx.serialization.Serializable

sealed interface Route {
  @Serializable data object Home : Route
  @Serializable data object Settings : Route
  @Serializable data object Category : Route
  @Serializable data class Preview(val id: Int) : Route
  @Serializable data class TheCategory(val id: Int) : Route
}
