package net.techandgraphics.hymn.ui.activity

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
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
      setOnExitAnimationListener { splashScreen ->
        ObjectAnimator.ofFloat(
          splashScreen.view,
          View.TRANSLATION_Y,
          0f, splashScreen.view.height.toFloat()
        ).apply {
          interpolator = DecelerateInterpolator()
          duration = 500L
          doOnEnd { splashScreen.remove() }
          start()
        }
      }
      setKeepOnScreenCondition { viewModel.state.value.completed }
    }
    setContent {
      HymnTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          AppScreen()
        }
      }
    }
  }
}
