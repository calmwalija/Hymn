package net.techandgraphics.hymn.ui.screen.settings

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri.parse
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Export
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import
import net.techandgraphics.hymn.ui.screen.settings.components.ApostleCreedDialog
import net.techandgraphics.hymn.ui.screen.settings.components.LordsPrayerDialog
import net.techandgraphics.hymn.ui.screen.settings.components.SettingContentComp
import net.techandgraphics.hymn.ui.screen.settings.components.SettingsSwitchComp
import net.techandgraphics.hymn.ui.screen.settings.components.SettingsTextComp
import net.techandgraphics.hymn.ui.screen.settings.export.share
import net.techandgraphics.hymn.ui.theme.ThemeConfigs
import java.io.File

@Composable
fun SettingsScreen(
  state: SettingsUiState,
  onEvent: (SettingsUiEvent) -> Unit,
  onThemeConfigs: (ThemeConfigs) -> Unit,
  channelFlow: Flow<SettingsChannelEvent>
) {

  val context = LocalContext.current
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"

  var progressStatus by remember { mutableStateOf(Import.ProgressStatus(-1, -1)) }
  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }

  var dynamicColor by remember { mutableStateOf(false) }
  var fontFamily by remember { mutableStateOf(false) }
  var isImporting by remember { mutableStateOf(false) }
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

  val jsonPicker = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
    uri?.let {
      onEvent(SettingsUiEvent.Import(it))
    }
  }

  LaunchedEffect(key1 = channelFlow) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      channelFlow.collect { event ->
        when (event) {
          is Import.Import -> when (event.status) {
            Import.Status.Wait -> {
              isImporting = true
              context.toast("Working on it, please wait ...")
            }

            Import.Status.Invalid -> {
              isImporting = false
              context.toast("Processing failed, this is an invalid file.")
            }

            Import.Status.Error -> {
              isImporting = false
              context.toast("Something went wrong, please try again")
            }

            Import.Status.Success -> {
              isImporting = false
              context.toast("Data has been restored successfully")
            }
          }

          is Export.Export -> context.share(event.file)
          is Import.Progress -> {
//            Log.e("TAG", "SettingsScreen: " + event.progressStatus)
            progressStatus = event.progressStatus
          }
        }
      }
    }
  }

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
        contract = GetContent()
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
      ) { onEvent(SettingsUiEvent.Export) }

      HorizontalDivider()

      SettingContentComp(
        title = "Import",
        description = "Easily bring in your data by importing exported files & get started with your information in no time.",
        onEvent = { if (!isImporting) jsonPicker.launch("application/json") },
        content = {
          Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) {
            if (isImporting) {
              if (progressStatus.currentProgress > 0) CircularProgressIndicator(
                progress = { progressStatus.currentProgress.toFloat().div(progressStatus.total) }
              ) else {
                CircularProgressIndicator()
                progressStatus = Import.ProgressStatus(-1, -1)
              }
            } else
              Icon(
                painter = painterResource(id = R.drawable.ic_import),
                contentDescription = null,
              )
          }
        }
      )
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
