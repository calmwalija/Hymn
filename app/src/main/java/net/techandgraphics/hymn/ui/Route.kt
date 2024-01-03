package net.techandgraphics.hymn.ui

enum class Route(val route: String = "", val title: String = "") {
  Home(title = "Home"),
  Miscellaneous(title = "Misc"),
  Category(title = "Category"),
  Search(title = "Search"),
  Read(route = "read/{id}", title = "read"),
  Categorisation(route = "categorisation/{id}", title = "Categorisation"),
}
