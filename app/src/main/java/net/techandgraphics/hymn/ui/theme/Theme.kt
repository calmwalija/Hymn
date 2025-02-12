package net.techandgraphics.hymn.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
  primary = AccentColor,
  onPrimary = AccentColor
)

private val LightColorScheme = lightColorScheme(
  primary = AccentColor,
  onPrimaryContainer = AccentColor,
  onPrimary = Color.Black
)

data class ThemeConfigs(
  val dynamicColor: Boolean? = null,
  val fontFamily: FontFamily? = null
)

@Composable
fun HymnTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  fontFamily: FontFamily = FontFamily.Default,
  content: @Composable () -> Unit,

) {
  val systemUiController = rememberSystemUiController()

  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as Activity).window
      systemUiController.setSystemBarsColor(color = colorScheme.surface)
      WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = !darkTheme
        isAppearanceLightNavigationBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = setTypography(fontFamily),
    content = content
  )
}
