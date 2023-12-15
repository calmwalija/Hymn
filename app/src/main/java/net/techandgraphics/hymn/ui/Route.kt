package net.techandgraphics.hymn.ui

enum class Route(val route: String = "", val title: String = "") {
  Home(title = "Home"),
  Manage(title = "Manage"),
  Category(title = "category"),
  Search(title = "search"),
  Read(route = "read/{id}", title = "read"),
  Categorisation(route = "categorisation/{id}", title = "categorisation"),
  Favourite(route = "favourite", title = "favourite"),
}
