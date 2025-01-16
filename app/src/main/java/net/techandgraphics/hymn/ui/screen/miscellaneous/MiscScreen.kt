package net.techandgraphics.hymn.ui.screen.miscellaneous

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri.parse
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.miscellaneous.settings.SettingsSwitchComp
import net.techandgraphics.hymn.ui.screen.miscellaneous.settings.SettingsTextComp
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent
import net.techandgraphics.hymn.ui.screen.preview.READ_FONT_SIZE_THRESH_HOLD
import net.techandgraphics.hymn.ui.screen.preview.READ_LINE_HEIGHT_THRESH_HOLD
import net.techandgraphics.hymn.ui.theme.ThemeConfigs
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiscScreen(
  state: MiscState,
  readEvent: (PreviewUiEvent) -> Unit,
  event: (MiscEvent) -> Unit,
  onThemeConfigs: (ThemeConfigs) -> Unit
) {

  val context = LocalContext.current
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"

  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }

  var darkTheme by remember { mutableStateOf(false) }
  var digitKeyboard by remember { mutableStateOf(false) }
  var dynamicColor by remember { mutableStateOf(false) }
  var fontFamily by remember { mutableStateOf(false) }

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
      text = "Settings",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
    )
    Card(
      elevation = CardDefaults.elevatedCardElevation(
        defaultElevation = 1.dp
      ),
      colors = CardDefaults.elevatedCardColors(),
      modifier = Modifier.padding(4.dp)
    ) {

      SettingsSwitchComp(
        drawableRes = R.drawable.ic_theme,
        title = "Theme",
        description = "Customize your experience by choosing between Light and Dark themes",
        isChecked = darkTheme,
        onCheckedChange = {
          darkTheme = it
          onThemeConfigs.invoke(ThemeConfigs(darkTheme = darkTheme))
        }
      )

      HorizontalDivider()

      SettingsSwitchComp(
        drawableRes = R.drawable.ic_keyboard,
        title = "Text Keyboard",
        description = "Toggle between the Numpad and Text Keyboard for easier input, depending on your needs.",
        isChecked = digitKeyboard,
        onCheckedChange = { digitKeyboard = it }
      )

      HorizontalDivider()

      SettingsSwitchComp(
        drawableRes = R.drawable.ic_color,
        title = "Colors",
        description = "Automatically change the color style based on your background wallpaper colors.",
        isChecked = dynamicColor,
        onCheckedChange = {
          dynamicColor = it
          onThemeConfigs.invoke(ThemeConfigs(dynamicColor = dynamicColor))
        }
      )

      HorizontalDivider()

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
        title = "Default Font",
        description = "Choose your preferred font family to personalize the app's appearance and make reading more comfortable.",
        isChecked = fontFamily,
        onCheckedChange = {
          fontPickerLauncher.launch("font/*")
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
      colors = CardDefaults.elevatedCardColors(),
      modifier = Modifier.padding(4.dp)
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
        colors = CardDefaults.elevatedCardColors(),
        modifier = Modifier.padding(4.dp)
      ) {

        val show = remember { mutableStateOf(false) }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              if (state.favorites.isEmpty()) {
                context toast context.getString(R.string.no_fav_hymn)
                return@clickable
              }
              show.value = true
            }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          BadgedBox(
            badge = {
              if (state.favorites.isNotEmpty())
                Badge(
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = Color.White
                ) {
                  Text(text = state.favorites.size.toString())
                }
            }
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_outline_favorite),
              contentDescription = null,
              modifier = Modifier.size(28.dp),
            )
          }
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Favorite Hymns",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.favorite),
              style = MaterialTheme.typography.bodyMedium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        HorizontalDivider()

        if (lordsPrayerShow) {
          ModalBottomSheet(onDismissRequest = { lordsPrayerShow = false }) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(horizontal = 16.dp)
            ) {
              Text(
                text = state.complementary.first().groupName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = state.complementary.first().content,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
                fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
              )
              Spacer(modifier = Modifier.height(32.dp))
            }
          }
        }

        SettingsTextComp(
          drawableRes = R.drawable.ic_prayer,
          title = state.complementary.first().groupName,
          description = state.complementary.first().content.replace("\n", ""),
        ) {
          lordsPrayerShow = true
        }

        HorizontalDivider()

        if (apostleCreedShow) {
          ModalBottomSheet(onDismissRequest = { apostleCreedShow = false }) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier.padding(horizontal = 16.dp)
            ) {
              Text(
                text = state.complementary.last().groupName,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = state.complementary.last().content,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
                fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
              )
              Spacer(modifier = Modifier.height(32.dp))
            }
          }
        }

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
