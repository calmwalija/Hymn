package net.techandgraphics.hymn.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var navController: NavController
  private val viewModel by viewModels<BaseViewModel>()

  @SuppressLint("SourceLockedOrientationActivity")
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.userPrefs.getBuild
      .asLiveData()
      .observe(this) {
        viewModel.onLoad(it != UserPrefs.BUILD)
      }
    installSplashScreen().apply {
      setKeepOnScreenCondition { viewModel.whenRead.value }
    }
    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    navController = findNavController(R.id.fragmentContainerView)
    binding.bottomNavigationView.setupWithNavController(navController)
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

  }


  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp() || super.onSupportNavigateUp()
  }

  override fun onResume() {
    val themeValue: Array<String> = resources.getStringArray(R.array.theme_values)
    when (PreferenceManager.getDefaultSharedPreferences(this)
      .getString(getString(R.string.theme_key), themeValue[0])
      .toString()) {
      themeValue[0] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
      themeValue[1] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
      themeValue[2] -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    super.onResume()
  }
}