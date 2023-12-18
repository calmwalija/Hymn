package net.techandgraphics.hymn.ui

enum class Route(val route: String = "", val title: String = "") {
  Home(title = "Home"),
  Miscellaneous(title = "Miscellaneous"),
  Category(title = "category"),
  Search(title = "search"),
  Read(route = "read/{id}", title = "read"),
  Categorisation(route = "categorisation/{id}", title = "categorisation"),
}
