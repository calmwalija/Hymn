package net.techandgraphics.hymn.domain.model

import net.techandgraphics.hymn.presentation.fragments.search.tagColor

data class Search(
  val id: Long = System.currentTimeMillis(),
  val query: String,
  val tag: String,
  val lang: String = "en"
) {
  val color = when (id.toString().takeLast(3).toInt()) {
    in 1..100 -> tagColor[0]
    in 101..200 -> tagColor[1]
    in 201..300 -> tagColor[2]
    in 301..400 -> tagColor[3]
    in 401..500 -> tagColor[4]
    in 501..600 -> tagColor[5]
    in 601..700 -> tagColor[6]
    in 701..800 -> tagColor[7]
    in 801..900 -> tagColor[8]
    in 901..1000 -> tagColor[9]
    else -> tagColor[4]
  }
}
