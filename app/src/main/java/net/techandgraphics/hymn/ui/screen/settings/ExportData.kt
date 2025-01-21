package net.techandgraphics.hymn.ui.screen.settings

import android.content.Context
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.local.entities.TimestampEntity
import net.techandgraphics.hymn.domain.model.Lyric
import java.io.File

data class ExportLyric(
  val lyricId: Int,
  val favorite: Boolean,
  val timestamp: Long,
)

fun Lyric.toExport() = ExportLyric(lyricId, favorite, timestamp)

data class Export(
  val currentTimeMillis: Long = System.currentTimeMillis(),
  val lyrics: List<ExportLyric>,
  val search: List<SearchEntity>,
  val timeSpent: List<TimeSpentEntity>,
  val timestamp: List<TimestampEntity>,
  val hashable: String
)

object ExportData {

  private const val FILENAME = "ExportData.json"
  private const val TYPE = "application/json"

  fun writeToInternalStorage(context: Context, jsonData: String): File {
    context.openFileOutput(FILENAME, Context.MODE_PRIVATE).use { outputStream ->
      outputStream.write(jsonData.toByteArray())
    }
    return File(context.filesDir, FILENAME)
  }

  fun shareFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    ShareCompat.IntentBuilder(context)
      .setType(TYPE)
      .setSubject("Shared files")
      .addStream(uri)
      .setChooserTitle("Share JSON File")
      .startChooser()
  }
}
