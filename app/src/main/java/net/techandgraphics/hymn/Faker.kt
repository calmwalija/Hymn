package net.techandgraphics.hymn

import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.domain.model.Category
import net.techandgraphics.hymn.domain.model.Lyric
import kotlin.random.Random

object Faker {

  val lyric =
    Lyric(
      lyricId = 971,
      categoryId = 17,
      categoryName = "The Christian Life: Love and Gratitude",
      number = 120,
      chorus = 0,
      content = "Holy, Holy, Holy!\r\nAll the saints adore Thee,\r\nCasting down their golden crowns\r\naround the glassy sea,\r\nCherubim and seraphim\r\nfalling down before Thee,\r\nWhich wert, and art, and\r\nevermore shalt be.",
      favorite = Random.nextBoolean(),
      title = "I Will Sing Of The Wondrous Story",
      lang = Lang.EN.lowercase(),
      timestamp = System.currentTimeMillis().minus(Random.nextInt(9000000, 12300000))
    )

  val category = Category(
    lyric = lyric, "39"
  )
}
