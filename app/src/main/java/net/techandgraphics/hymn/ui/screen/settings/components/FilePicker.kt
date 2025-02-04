package net.techandgraphics.hymn.ui.screen.settings.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import net.techandgraphics.hymn.fontFile
import java.io.File

enum class FileType(val ofType: String) { Font(ofType = "font/*"), }

@Composable
fun FilePicker(
  filePickerShow: Boolean,
  fileType: FileType = FileType.Font,
  onEvent: (File, String?) -> Unit
) {
  val context = LocalContext.current
  val fontPickerLauncher = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
    uri?.let { selectedUri ->
      onEvent(getFile(selectedUri, context), selectedUri.name())
    }
  }
  if (filePickerShow) fontPickerLauncher.launch(fileType.ofType)
}

private fun Uri.name() = lastPathSegment?.substringAfterLast(":")

private fun getFile(uri: Uri, context: Context): File {
  val tempFile = context.fontFile()
  context.contentResolver.openInputStream(uri)?.use { input ->
    tempFile.outputStream().use { output ->
      input.copyTo(output)
    }
  }
  return tempFile
}
