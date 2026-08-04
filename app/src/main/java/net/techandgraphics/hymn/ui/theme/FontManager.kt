package net.techandgraphics.hymn.ui.theme

import android.content.Context
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object FontManager {

  val defaultFontFamily: FontFamily = FontFamily.Default

  enum class Font(val font: String) {
    Default(font = "System Default"),
    Outfit(font = "Outfit"),
    MerriWeatherSans(font = "MerriWeather Sans"),
    GoogleSans(font = "Google Sans"),
  }

  fun merriWeatherSans(context: Context): FontFamily = runCatching {
    FontFamily(
      Font(
        path = "fonts/MerriWeatherSans-Regular.ttf",
        assetManager = context.assets,
        weight = FontWeight.Normal,
      ),
      Font(
        path = "fonts/MerriWeatherSans-Bold.ttf",
        assetManager = context.assets,
        weight = FontWeight.Bold,
      ),
    )
  }.getOrElse { FontFamily.Default }

  fun googleSans(context: Context): FontFamily = runCatching {
    FontFamily(
      Font(
        path = "fonts/GoogleSans-Regular.ttf",
        assetManager = context.assets,
        weight = FontWeight.Normal,
      ),
      Font(
        path = "fonts/GoogleSans-Medium.ttf",
        assetManager = context.assets,
        weight = FontWeight.Medium,
      ),
    )
  }.getOrElse { FontFamily.Default }

  fun outfit(context: Context): FontFamily = runCatching {
    FontFamily(
      Font(
        path = "fonts/Outfit-Medium.ttf",
        assetManager = context.assets,
        weight = FontWeight.Medium,
      ),
      Font(
        path = "fonts/Outfit-Bold.ttf",
        assetManager = context.assets,
        weight = FontWeight.Bold,
      ),
    )
  }.getOrElse { FontFamily.Default }

  fun getFontFamilyFromName(fontName: String, context: Context? = null): FontFamily =
    runCatching {
      when (fontName) {
        "Outfit" -> context?.let { outfit(it) } ?: FontFamily.Default
        "MerriWeather Sans" -> context?.let { merriWeatherSans(it) } ?: FontFamily.Default
        "Google Sans" -> context?.let { googleSans(it) } ?: FontFamily.Default
        else -> FontFamily.Default
      }
    }.getOrElse { FontFamily.Default }

  fun description(fontName: String): String = when (fontName) {
    "System Default" -> "Device default"
    "Outfit" -> "Professional & modern"
    "MerriWeather Sans" -> "Clean & readable"
    "Google Sans" -> "Clean & modern"
    else -> "Custom font"
  }
}

enum class AppTheme {
  SYSTEM,
  LIGHT,
  DARK,
  ;

  fun label(): String = when (this) {
    SYSTEM -> "System"
    LIGHT -> "Light"
    DARK -> "Dark"
  }

  companion object {
    fun fromStorage(value: String?): AppTheme =
      entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
  }
}
