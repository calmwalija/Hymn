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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.ui.activity.MainActivityUiEvent.DynamicColor
import net.techandgraphics.hymn.ui.activity.MainActivityUiEvent.FontStyle
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
      val state = viewModel.state.collectAsState().value

      HymnTheme(
        dynamicColor = state.dynamicColorEnabled,
        fontFamily = state.fontFamily
      ) {
        if (state.showStartupFailure) StartupFailureDialog { finish() }
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          Scaffold {
            AppScreen(
              paddingValues = it,
              onThemeConfigs = { config ->
                config.dynamicColor?.let { viewModel.onEvent(DynamicColor(it)) }
                viewModel.onEvent(FontStyle(config.fontFamily))
              }
            )
          }
        }
      }
    }
  }
}
