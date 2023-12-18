package net.techandgraphics.hymn.domain.model

import net.techandgraphics.hymn.ui.theme.AccentColor

data class Search(
  val id: Long = System.currentTimeMillis(),
  val query: String,
  val tag: String,
  val lang: String = "en"
) {

  val color = AccentColor

//    when (id.toString().takeLast(2).toInt()) {
//    in 1..10 -> tagColor[0]
//    in 11..20 -> tagColor[1]
//    in 21..30 -> tagColor[2]
//    in 31..40 -> tagColor[3]
//    in 41..50 -> tagColor[4]
//    in 51..60 -> tagColor[5]
//    in 61..70 -> tagColor[6]
//    in 71..80 -> tagColor[7]
//    in 81..90 -> tagColor[8]
//    in 91..100 -> tagColor[9]
//    else -> tagColor[4]
//  }
}
