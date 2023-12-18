package net.techandgraphics.hymn

import net.techandgraphics.hymn.data.local.entities.LyricEntity

object Faker {

  val lyricEntity = LyricEntity(
    lyricId = 1,
    categoryId = 1,
    categoryName = "For the Service",
    number = 0,
    chorus = 0,
    content = "Lorem ipsum dolor sit,\nconsectetur adipiscing elit, \nsed do eiusmod tempor incididunt ut\nlabore et dolore magna aliqua.",
    topPick = "lorem",
    title = "Lorem ipsum dolor sit amet",
  )
}
