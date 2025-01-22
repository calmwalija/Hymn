package net.techandgraphics.hymn.ui.screen.settings

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri.parse
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.ui.screen.settings.components.ApostleCreedDialog
import net.techandgraphics.hymn.ui.screen.settings.components.LordsPrayerDialog
import net.techandgraphics.hymn.ui.screen.settings.components.SettingsSwitchComp
import net.techandgraphics.hymn.ui.screen.settings.components.SettingsTextComp
import net.techandgraphics.hymn.ui.screen.settings.export.ExportData
import net.techandgraphics.hymn.ui.screen.settings.export.fileName
import net.techandgraphics.hymn.ui.screen.settings.export.hash
import net.techandgraphics.hymn.ui.screen.settings.export.share
import net.techandgraphics.hymn.ui.screen.settings.export.toHash
import net.techandgraphics.hymn.ui.screen.settings.export.write
import net.techandgraphics.hymn.ui.theme.ThemeConfigs
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  state: SettingsUiState,
  onEvent: (SettingsUiEvent) -> Unit,
  onThemeConfigs: (ThemeConfigs) -> Unit
) {

  val context = LocalContext.current
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"

  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }

  var dynamicColor by remember { mutableStateOf(false) }
  var fontFamily by remember { mutableStateOf(false) }
  var showFilePicker by remember { mutableStateOf(false) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 8.dp),
  ) {
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = "About App",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
    )
    Card(
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      modifier = Modifier.padding(4.dp)
    ) {
      SettingsTextComp(
        drawableRes = R.drawable.ic_developer,
        title = "Developers",
        description = stringResource(R.string.about),
      )

      HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

      getAppVersion(context)?.let {
        SettingsTextComp(
          drawableRes = R.drawable.ic_info,
          title = "App Info",
          description = "Version ${it.name}.3725",
        )
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
      text = "Appearance",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
    )
    Card(
      elevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 1.dp
      ),
      colors = CardDefaults.elevatedCardColors(), modifier = Modifier.padding(4.dp)
    ) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        SettingsSwitchComp(
          drawableRes = R.drawable.ic_color,
          title = "Wallpaper Colors",
          description = "Automatically change the color style based on your background wallpaper colors.",
          isChecked = dynamicColor,
          onCheckedChange = {
            dynamicColor = it
            onThemeConfigs.invoke(ThemeConfigs(dynamicColor = dynamicColor))
          }
        )
        HorizontalDivider()
      }

      val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
      ) { uri ->
        uri?.let { selectedUri ->
          val tempFile = File(context.cacheDir, "temp_font.ttf")
          context.contentResolver.openInputStream(selectedUri)?.use { input ->
            tempFile.outputStream().use { output ->
              input.copyTo(output)
            }
          }
          onThemeConfigs.invoke(
            ThemeConfigs(
              fontFamily = FontFamily(
                Typeface.createFromFile(
                  tempFile
                )
              )
            )
          )
        }
      }

      SettingsSwitchComp(
        drawableRes = R.drawable.ic_font_face,
        title = "App Font Style",
        description = "Choose your preferred font family to personalize the app's appearance and make reading more comfortable.",
        isChecked = fontFamily,
        onCheckedChange = {
          fontPickerLauncher.launch("font/*")
        }
      )
    }
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = "Hymn Data",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
    )
    Card(
      elevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 1.dp
      ),
      colors = CardDefaults.elevatedCardColors(), modifier = Modifier.padding(4.dp)
    ) {

      SettingsTextComp(
        drawableRes = R.drawable.ic_upload,
        title = "Export",
        description = "Quickly transfer your data by exporting it in JSON format.",
      ) {

        val currentTimeMillis = System.currentTimeMillis()
        val gson = Gson()
        val toExportData = ExportData(
          currentTimeMillis = currentTimeMillis,
          favorites = state.favExport,
          timeSpent = state.timeSpentExport,
          timestamp = state.timeStampExport,
          search = state.searchExport,
        )
        val hashable = currentTimeMillis.hash(toExportData.toHash())
        val jsonToExport = gson.toJson(toExportData.copy(hashable = hashable))
        val file = context.write(jsonToExport, fileName())
        context.share(file)
      }

      HorizontalDivider()

      val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
      ) { uri ->
        uri?.let { selectedUri ->
          val tempFile = File(context.cacheDir, "data.json")
          context.contentResolver.openInputStream(selectedUri)?.use { input ->
            tempFile.outputStream().use { output ->
              input.copyTo(output)
            }
          }

          onEvent(SettingsUiEvent.Import(tempFile))
        }
      }
      SettingsTextComp(
        drawableRes = R.drawable.ic_import,
        title = "Import",
        description = "Easily bring in your data by importing exported files & get started with your information in no time.",
      ) {
        fontPickerLauncher.launch("application/json")
      }
    }

    Spacer(modifier = Modifier.height(32.dp))

    Text(
      text = "Contact",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
    )
    Card(
      elevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 1.dp
      ),
      colors = CardDefaults.elevatedCardColors(), modifier = Modifier.padding(4.dp)
    ) {

      SettingsTextComp(
        drawableRes = R.drawable.ic_feedback,
        title = "Feedback",
        description = stringResource(id = R.string.feedback),
      ) {
        context.startActivity(Intent(ACTION_VIEW).setData(parse(whatsAppUrl)))
      }

      HorizontalDivider()

      SettingsTextComp(
        drawableRes = R.drawable.ic_rate,
        title = "Feedback",
        description = stringResource(id = R.string.rate),
      ) {
        context.startActivity(Intent(ACTION_VIEW).setData(parse(playStoreUrl)))
      }
    }
    if (state.complementary.isNotEmpty()) {
      Spacer(modifier = Modifier.height(32.dp))
      Text(
        text = "Complementary",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
      )
      Card(
        elevation = CardDefaults.elevatedCardElevation(
          defaultElevation = 1.dp
        ),
        colors = CardDefaults.elevatedCardColors(), modifier = Modifier.padding(4.dp)
      ) {
        SettingsTextComp(
          drawableRes = R.drawable.ic_prayer,
          title = state.complementary.first().groupName,
          description = state.complementary.first().content.replace("\n", ""),
        ) {
          lordsPrayerShow = true
        }

        HorizontalDivider()

        if (lordsPrayerShow) LordsPrayerDialog(state) { lordsPrayerShow = false }
        if (apostleCreedShow) ApostleCreedDialog(state) { apostleCreedShow = false }

        SettingsTextComp(
          drawableRes = R.drawable.ic_creed,
          title = state.complementary.last().groupName,
          description = state.complementary.last().content.replace("\n", ""),
        ) {
          apostleCreedShow = true
        }
      }
    }
  }
}
