package net.techandgraphics.hymn.ui.screen.settings.export

import android.content.Context
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import java.io.File
import java.security.MessageDigest
import java.util.UUID

private const val TYPE = "application/json"

fun fileName() = UUID.randomUUID().toString().plus(".json")

fun Context.write(jsonData: String, fileName: String): File {
  openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
    outputStream.write(jsonData.toByteArray())
  }
  return File(filesDir, fileName)
}

fun Context.share(file: File) {
  val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
  ShareCompat.IntentBuilder(this)
    .setType(TYPE)
    .setSubject("Shared files")
    .addStream(uri)
    .setChooserTitle("Share JSON File")
    .startChooser()
}

fun Long.hash(text: String, algorithm: String = "SHA-512"): String {
  val theKey = toString()
    .substring(5, toString().length.minus(3))
    .toInt()
    .times(toString().sumOf { it.digitToInt() })
    .toString()
  val bytes =
    MessageDigest.getInstance(algorithm).digest(theKey.plus(text).plus(theKey).toByteArray())
  return bytes.joinToString("") { "%02x".format(it) }
}

fun ExportData.toHash(): String = Gson().toJson(favorites + search + currentTimeMillis)
