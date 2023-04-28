package net.techandgraphics.hymn

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService

class NetworkConnection(private val context: Context) {

  @Suppress("DEPRECATION")
  fun ifConnected(): Boolean {

    val connectivityManager = getSystemService(context, ConnectivityManager::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val activeNetwork = connectivityManager?.activeNetwork ?: return false
      val networkCapability =
        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
      return when {
        networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
          networkCapability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
      }
    } else {
      connectivityManager?.activeNetworkInfo?.run {
        return when (type) {
          ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE -> true
          else -> false
        }
      }
    }
    return false
  }
}
