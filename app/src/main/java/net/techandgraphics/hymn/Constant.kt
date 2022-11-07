package net.techandgraphics.hymn

import androidx.annotation.DrawableRes

data class Category(
  val categoryId: Int,
  @DrawableRes val drawableRes: Int
)

object Constant {
  val images = arrayListOf(
    Category(1, R.drawable.word_of_god),
    Category(2, R.drawable.creation),
    Category(3, R.drawable.providence),
    Category(4, R.drawable.redemption),
    Category(5, R.drawable.incarnation),
    Category(6, R.drawable.example),
    Category(7, R.drawable.suffering_and_death),
    Category(8, R.drawable.resurrection),
    Category(9, R.drawable.glory),
    Category(10, R.drawable.help),
    Category(11, R.drawable.coming_again),
    Category(12, R.drawable.praise),
    Category(13, R.drawable.holy_spirit),
    Category(14, R.drawable.word_of_god),
    Category(15, R.drawable.gospel),
    Category(16, R.drawable.repentance),
    Category(17, R.drawable.love_and_gratitude),
    Category(18, R.drawable.joy_and_peace),
    Category(19, R.drawable.faith),
    Category(20, R.drawable.holiness),
    Category(21, R.drawable.service),
    Category(22, R.drawable.conflict_and_templation),
    Category(23, R.drawable.pilgrimage),
    Category(24, R.drawable.death),
    Category(25, R.drawable.heavenly_glory),
    Category(26, R.drawable.prayer),
    Category(27, R.drawable.church),
    Category(28, R.drawable.morning_prayer),
    Category(29, R.drawable.evening_player),
    Category(30, R.drawable.lords_day),
    Category(31, R.drawable.house_of_the_lord),
    Category(32, R.drawable.worship_and_praise),
    Category(33, R.drawable.baptism),
    Category(34, R.drawable.church),
    Category(35, R.drawable.glory),
    Category(36, R.drawable.conflict_and_templation),
    Category(37, R.drawable.praise),
    Category(38, R.drawable.redemption),
    Category(38, R.drawable.resurrection),
    Category(39, R.drawable.gospel),
    Category(40, R.drawable.new_year),
    Category(41, R.drawable.seed_time_and_harvest),
    Category(42, R.drawable.lords_day),
    Category(43, R.drawable.holiness),
    Category(44, R.drawable.pilgrimage),
    Category(45, R.drawable.death),
    Category(46, R.drawable.faith),
    Category(47, R.drawable.lords_day),
    Category(48, R.drawable.providence),
    Category(49, R.drawable.resurrection),
    Category(50, R.drawable.faith),
    Category(51, R.drawable.example),
    Category(52, R.drawable.holiness),
    Category(53, R.drawable.death),
    Category(54, R.drawable.gospel),
    Category(55, R.drawable.holy_trinity),
    Category(56, R.drawable.conflict_and_templation),

    Category(56 + 1, R.drawable.word_of_god),
    Category(56 + 2, R.drawable.creation),
    Category(56 + 3, R.drawable.providence),
    Category(56 + 4, R.drawable.redemption),
    Category(56 + 5, R.drawable.incarnation),
    Category(56 + 6, R.drawable.example),
    Category(56 + 7, R.drawable.suffering_and_death),
    Category(56 + 8, R.drawable.resurrection),
    Category(56 + 9, R.drawable.glory),
    Category(56 + 10, R.drawable.help),
    Category(56 + 11, R.drawable.coming_again),
    Category(56 + 12, R.drawable.praise),
    Category(56 + 13, R.drawable.holy_spirit),
    Category(56 + 14, R.drawable.word_of_god),
    Category(56 + 15, R.drawable.gospel),
    Category(56 + 16, R.drawable.repentance),
    Category(56 + 17, R.drawable.love_and_gratitude),
    Category(56 + 18, R.drawable.joy_and_peace),
    Category(56 + 19, R.drawable.faith),
    Category(56 + 20, R.drawable.holiness),
    Category(56 + 21, R.drawable.service),
    Category(56 + 22, R.drawable.conflict_and_templation),
    Category(56 + 23, R.drawable.pilgrimage),
    Category(56 + 24, R.drawable.death),
    Category(56 + 25, R.drawable.heavenly_glory),
    Category(56 + 26, R.drawable.prayer),
    Category(56 + 27, R.drawable.church),
    Category(56 + 28, R.drawable.morning_prayer),
    Category(56 + 29, R.drawable.evening_player),
    Category(56 + 30, R.drawable.lords_day),
    Category(56 + 31, R.drawable.house_of_the_lord),
    Category(56 + 32, R.drawable.worship_and_praise),
    Category(56 + 33, R.drawable.baptism),
    Category(56 + 34, R.drawable.church),
    Category(56 + 35, R.drawable.glory),
    Category(56 + 36, R.drawable.conflict_and_templation),
    Category(56 + 37, R.drawable.praise),
    Category(56 + 38, R.drawable.redemption),
    Category(56 + 38, R.drawable.resurrection),
    Category(56 + 39, R.drawable.gospel),
    Category(56 + 40, R.drawable.new_year),
    Category(56 + 41, R.drawable.seed_time_and_harvest),
    Category(56 + 42, R.drawable.lords_day),
    Category(56 + 43, R.drawable.holiness),
    Category(56 + 44, R.drawable.pilgrimage),
    Category(56 + 45, R.drawable.death),
    Category(56 + 46, R.drawable.faith),
    Category(56 + 47, R.drawable.lords_day),
    Category(56 + 48, R.drawable.providence),
    Category(56 + 49, R.drawable.resurrection),
    Category(56 + 50, R.drawable.faith),
    Category(56 + 51, R.drawable.example),
    Category(56 + 52, R.drawable.holiness),
    Category(56 + 53, R.drawable.death),
    Category(56 + 54, R.drawable.gospel),
    Category(56 + 55, R.drawable.holy_trinity),
    Category(56 + 56, R.drawable.conflict_and_templation),
  )


  const val DOMAIN_URI_PREFIX = "https://hymn.page.link"
  const val DEEP_LINK = "https://techandgraphics.net/"
  const val LOGO_URL = "https://techandgraphics.net/image/hymn/logo2.jpg"

  const val RESTART = "RESTART"


}