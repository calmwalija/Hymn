package net.techandgraphics.hymn.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.ui.screen.app.AppScreen
import net.techandgraphics.hymn.ui.theme.HymnTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  private val viewModel by viewModels<MainActivityViewModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
//    viewModel.init()
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
