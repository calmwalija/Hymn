package net.techandgraphics.hymn.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.core.view.WindowCompat

data class ThemeConfigs(
  val dynamicColor: Boolean? = null,
  val fontFamily: FontFamily? = null,
  val appTheme: AppTheme? = null,
)

@Composable
fun HymnTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  fontFamily: FontFamily = FontFamily.Default,
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }

    darkTheme -> HymnDarkColors
    else -> HymnLightColors
  }

  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      // The bars stay transparent (edge-to-edge is set up in MainActivity); all
      // this needs to do is keep the status/nav icons legible against content.
      val window = (view.context as Activity).window
      WindowCompat.getInsetsController(window, window.decorView).apply {
        isAppearanceLightStatusBars = !darkTheme
        isAppearanceLightNavigationBars = !darkTheme
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = setTypography(fontFamily),
    shapes = HymnShapes,
    content = content,
  )
}
