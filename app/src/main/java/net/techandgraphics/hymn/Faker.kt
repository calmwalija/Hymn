package net.techandgraphics.hymn

import net.techandgraphics.hymn.data.local.Lang
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
      content = "I will sing of the wondrous story Of the Christ who died for me,",
      favorite = Random.nextBoolean(),
      title = "I Will Sing Of The Wondrous Story",
      lang = Lang.EN.lowercase(),
      timestamp = System.currentTimeMillis()
    )
}
