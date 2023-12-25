package net.techandgraphics.hymn

import androidx.annotation.DrawableRes
import net.techandgraphics.hymn.data.local.Lang.CH
import net.techandgraphics.hymn.data.local.entities.SearchEntity

data class CategoryRes(val categoryId: Int, @DrawableRes val drawableRes: Int)

object Constant {
  val images = arrayListOf(
    CategoryRes(1, R.drawable.im_word_of_god),
    CategoryRes(2, R.drawable.im_creation),
    CategoryRes(3, R.drawable.im_providence),
    CategoryRes(4, R.drawable.im_redemption),
    CategoryRes(5, R.drawable.im_incarnation),
    CategoryRes(6, R.drawable.im_example),
    CategoryRes(7, R.drawable.im_suffering_and_death),
    CategoryRes(8, R.drawable.im_resurrection),
    CategoryRes(9, R.drawable.im_glory),
    CategoryRes(10, R.drawable.im_help),
    CategoryRes(11, R.drawable.im_coming_again),
    CategoryRes(12, R.drawable.im_praise),
    CategoryRes(13, R.drawable.im_holy_spirit),
    CategoryRes(14, R.drawable.im_word_of_god),
    CategoryRes(15, R.drawable.im_gospel),
    CategoryRes(16, R.drawable.im_repentance),
    CategoryRes(17, R.drawable.im_love_and_gratitude),
    CategoryRes(18, R.drawable.im_joy_and_peace),
    CategoryRes(19, R.drawable.im_faith),
    CategoryRes(20, R.drawable.im_holiness),
    CategoryRes(21, R.drawable.im_service),
    CategoryRes(22, R.drawable.im_conflict_and_templation),
    CategoryRes(23, R.drawable.im_pilgrimage),
    CategoryRes(24, R.drawable.im_death),
    CategoryRes(25, R.drawable.im_heavenly_glory),
    CategoryRes(26, R.drawable.im_prayer),
    CategoryRes(27, R.drawable.im_church),
    CategoryRes(28, R.drawable.im_morning_prayer),
    CategoryRes(29, R.drawable.im_evening_player),
    CategoryRes(30, R.drawable.im_lords_day),
    CategoryRes(31, R.drawable.im_house_of_the_lord),
    CategoryRes(32, R.drawable.im_worship_and_praise),
    CategoryRes(33, R.drawable.im_baptism),
    CategoryRes(34, R.drawable.im_church),
    CategoryRes(35, R.drawable.im_glory),
    CategoryRes(36, R.drawable.im_conflict_and_templation),
    CategoryRes(37, R.drawable.im_praise),
    CategoryRes(38, R.drawable.im_redemption),
    CategoryRes(38, R.drawable.im_resurrection),
    CategoryRes(39, R.drawable.im_gospel),
    CategoryRes(40, R.drawable.im_new_year),
    CategoryRes(41, R.drawable.im_seed_time_and_harvest),
    CategoryRes(42, R.drawable.im_lords_day),
    CategoryRes(43, R.drawable.im_holiness),
    CategoryRes(44, R.drawable.im_pilgrimage),
    CategoryRes(45, R.drawable.im_death),
    CategoryRes(46, R.drawable.im_faith),
    CategoryRes(47, R.drawable.im_lords_day),
    CategoryRes(48, R.drawable.im_providence),
    CategoryRes(49, R.drawable.im_resurrection),
    CategoryRes(50, R.drawable.im_church),
    CategoryRes(51, R.drawable.im_example),
    CategoryRes(52, R.drawable.im_holiness),
    CategoryRes(53, R.drawable.im_death),
    CategoryRes(54, R.drawable.im_gospel),
    CategoryRes(55, R.drawable.im_holy_trinity),
    CategoryRes(56, R.drawable.im_conflict_and_templation),

    CategoryRes(56 + 1, R.drawable.im_word_of_god),
    CategoryRes(56 + 2, R.drawable.im_creation),
    CategoryRes(56 + 3, R.drawable.im_providence),
    CategoryRes(56 + 4, R.drawable.im_redemption),
    CategoryRes(56 + 5, R.drawable.im_incarnation),
    CategoryRes(56 + 6, R.drawable.im_example),
    CategoryRes(56 + 7, R.drawable.im_suffering_and_death),
    CategoryRes(56 + 8, R.drawable.im_resurrection),
    CategoryRes(56 + 9, R.drawable.im_glory),
    CategoryRes(56 + 10, R.drawable.im_help),
    CategoryRes(56 + 11, R.drawable.im_coming_again),
    CategoryRes(56 + 12, R.drawable.im_praise),
    CategoryRes(56 + 13, R.drawable.im_holy_spirit),
    CategoryRes(56 + 14, R.drawable.im_word_of_god),
    CategoryRes(56 + 15, R.drawable.im_gospel),
    CategoryRes(56 + 16, R.drawable.im_repentance),
    CategoryRes(56 + 17, R.drawable.im_love_and_gratitude),
    CategoryRes(56 + 18, R.drawable.im_joy_and_peace),
    CategoryRes(56 + 19, R.drawable.im_faith),
    CategoryRes(56 + 20, R.drawable.im_holiness),
    CategoryRes(56 + 21, R.drawable.im_service),
    CategoryRes(56 + 22, R.drawable.im_conflict_and_templation),
    CategoryRes(56 + 23, R.drawable.im_pilgrimage),
    CategoryRes(56 + 24, R.drawable.im_death),
    CategoryRes(56 + 25, R.drawable.im_heavenly_glory),
    CategoryRes(56 + 26, R.drawable.im_prayer),
    CategoryRes(56 + 27, R.drawable.im_church),
    CategoryRes(56 + 28, R.drawable.im_morning_prayer),
    CategoryRes(56 + 29, R.drawable.im_evening_player),
    CategoryRes(56 + 30, R.drawable.im_lords_day),
    CategoryRes(56 + 31, R.drawable.im_house_of_the_lord),
    CategoryRes(56 + 32, R.drawable.im_worship_and_praise),
    CategoryRes(56 + 33, R.drawable.im_baptism),
    CategoryRes(56 + 34, R.drawable.im_church),
    CategoryRes(56 + 35, R.drawable.im_glory),
    CategoryRes(56 + 36, R.drawable.im_conflict_and_templation),
    CategoryRes(56 + 37, R.drawable.im_praise),
    CategoryRes(56 + 38, R.drawable.im_redemption),
    CategoryRes(56 + 38, R.drawable.im_resurrection),
    CategoryRes(56 + 39, R.drawable.im_gospel),
    CategoryRes(56 + 40, R.drawable.im_new_year),
    CategoryRes(56 + 41, R.drawable.im_seed_time_and_harvest),
    CategoryRes(56 + 42, R.drawable.im_lords_day),
    CategoryRes(56 + 43, R.drawable.im_holiness),
    CategoryRes(56 + 44, R.drawable.im_pilgrimage),
    CategoryRes(56 + 45, R.drawable.im_death),
    CategoryRes(56 + 46, R.drawable.im_faith),
    CategoryRes(56 + 47, R.drawable.im_lords_day),
    CategoryRes(56 + 48, R.drawable.im_providence),
    CategoryRes(56 + 49, R.drawable.im_resurrection),
    CategoryRes(56 + 50, R.drawable.im_faith),
    CategoryRes(56 + 51, R.drawable.im_example),
    CategoryRes(56 + 52, R.drawable.im_holiness),
    CategoryRes(56 + 53, R.drawable.im_death),
    CategoryRes(56 + 54, R.drawable.im_gospel),
    CategoryRes(56 + 55, R.drawable.im_holy_trinity),
    CategoryRes(56 + 56, R.drawable.im_conflict_and_templation),
    CategoryRes(56 + 57, R.drawable.im_hope_and_comfort),
    CategoryRes(56 + 58, R.drawable.im_faithfulness_of_god),
    CategoryRes(56 + 59, R.drawable.im_joy_and_peace),
    CategoryRes(56 + 60, R.drawable.im_our_love_for_god),
    CategoryRes(56 + 61, R.drawable.im_our_love_for_god),
    CategoryRes(56 + 62, R.drawable.im_contemporary_hymns),
    CategoryRes(56 + 63, R.drawable.im_community_in_christ),
  )

  val searchEntityTags = arrayListOf(
    SearchEntity(query = "jesus loves me", tag = "jesuslovesme"),
    SearchEntity(query = "amazing grace", tag = "amazinggrace"),
    SearchEntity(query = "o happy day", tag = "ohappyday"),
    SearchEntity(query = "what a friend", tag = "whatafriend"),
    SearchEntity(query = "blessed assurance", tag = "blessedassurance"),

    SearchEntity(query = "unakhetsedwa mwazi", tag = "unakhetsedwamwazi", lang = CH.lowercase()),
    SearchEntity(query = "mbuye yesu lowani", tag = "mbuyeyesulowani", lang = CH.lowercase()),
    SearchEntity(query = "wina atikonda ife", tag = "winaatikondaife", lang = CH.lowercase()),
    SearchEntity(query = "pokhala mtendere", tag = "pokhalamtendere", lang = CH.lowercase()),
    SearchEntity(query = "mtima wa mbuyako", tag = "mtimawambuyako", lang = CH.lowercase()),
  )

  val ofTheDay = arrayListOf(
    R.drawable.of_the_day1420830,
    R.drawable.of_the_day3123468,
    R.drawable.of_the_day3587237,
    R.drawable.of_the_day5842978,
  )
}
