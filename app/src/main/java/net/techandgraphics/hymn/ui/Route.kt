package net.techandgraphics.hymn.ui

import kotlinx.serialization.Serializable

sealed interface Route {
  @Serializable data object Home : Route
  @Serializable data object Mixed : Route
  @Serializable data object Category : Route
  @Serializable data object Search : Route
  @Serializable data class Read(val id: Int) : Route
  @Serializable data class Categorisation(val id: Int) : Route
}
