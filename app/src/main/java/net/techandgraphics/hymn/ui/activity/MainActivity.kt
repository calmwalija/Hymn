package net.techandgraphics.hymn.ui.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.ui.screen.app.AppScreen
import net.techandgraphics.hymn.ui.theme.HymnTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainActivityViewModel>()
  private val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (Build.VERSION.SDK_INT >= 33) launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    installSplashScreen().apply {
      setKeepOnScreenCondition { viewModel.state.value.completed }
    }
    setContent {
      var darkTheme by remember { mutableStateOf(false) }
      var dynamicColor by remember { mutableStateOf(false) }
      var fontFamily by remember { mutableStateOf<FontFamily>(FontFamily.Default) }

      HymnTheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        fontFamily = fontFamily
      ) {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          AppScreen(
            onThemeConfigs = { config ->
              config.darkTheme?.let { darkTheme = it }
              config.dynamicColor?.let { dynamicColor = it }
              config.fontFamily?.let { fontFamily = it }
            }
          )
        }
      }
    }
  }
}
