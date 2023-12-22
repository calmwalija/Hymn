package net.techandgraphics.hymn

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.pm.PackageInfoCompat

data class AppVersion(val name: String, val number: Long)

fun getAppVersion(context: Context): AppVersion? {
  return try {
    val packageManager = context.packageManager
    val packageName = context.packageName
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
    } else packageManager.getPackageInfo(packageName, 0)
    AppVersion(
      name = packageInfo.versionName,
      number = PackageInfoCompat.getLongVersionCode(packageInfo),
    )
  } catch (e: Exception) {
    null
  }
}
