package net.techandgraphics.hymn.ui.screen.settings

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NavigationRow
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Export
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import
import net.techandgraphics.hymn.ui.screen.settings.export.share
import net.techandgraphics.hymn.ui.theme.AppTheme
import net.techandgraphics.hymn.ui.theme.FontManager
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  state: SettingsUiState,
  onBack: () -> Unit,
  onEvent: (SettingsEvent) -> Unit,
  channelFlow: Flow<SettingsChannelEvent>,
) {
  val context = LocalContext.current
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"
  val translationEntries = remember { context.resources.getStringArray(R.array.translation_entries).toList() }
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  val scrollBehavior =
    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  var isImporting by remember { mutableStateOf(false) }
  var showFontDialog by remember { mutableStateOf(false) }
  var showThemeDialog by remember { mutableStateOf(false) }
  var showLyricSizeDialog by remember { mutableStateOf(false) }
  var showTranslationDialog by remember { mutableStateOf(false) }
  var showResetConfirm by remember { mutableStateOf(false) }

  val jsonPicker = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
    uri?.let { onEvent(SettingsEvent.Import(it)) }
  }

  LaunchedEffect(channelFlow) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      channelFlow.collect { event ->
        when (event) {
          is Import.Import -> when (event.status) {
            Import.Status.Wait -> {
              isImporting = true
              context.toast("Working on it, please wait …")
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
          is Import.Progress -> Unit
          is SettingsChannelEvent.FontStyle ->
            onEvent(SettingsEvent.FontStyle.Apply(event.fontFamily))
        }
      }
    }
  }

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.nav_settings),
        onBack = onBack,
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = Space.md),
    ) {
      SectionLabel(stringResource(R.string.settings_appearance))
      SettingsCard {
        // SYSTEM is the stored default, but the old Light/Dark pair could not
        // represent it — a fresh install showed "Light" selected while actually
        // following the system.
        NavigationRow(
          title = stringResource(R.string.settings_theme),
          subtitle = state.appTheme.label(),
          leading = { RowIcon(R.drawable.ic_color) },
          onClick = { showThemeDialog = true },
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          Spacer(Modifier.height(Space.md))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = stringResource(R.string.settings_dynamic_color),
                style = MaterialTheme.typography.titleSmall,
              )
              Text(
                text = stringResource(R.string.settings_dynamic_color_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            Switch(
              checked = state.dynamicColor,
              onCheckedChange = { onEvent(SettingsEvent.DynamicColor(it)) },
            )
          }
        }

        Spacer(Modifier.height(Space.xs))
        NavigationRow(
          title = stringResource(R.string.settings_font),
          subtitle = state.fontFamily,
          leading = { RowIcon(R.drawable.ic_font_face) },
          onClick = { showFontDialog = true },
        )
      }

      SectionLabel(stringResource(R.string.settings_reading))
      SettingsCard {
        NavigationRow(
          title = stringResource(R.string.settings_lyric_size),
          subtitle = LyricSizePreset.label(state.fontSize),
          leading = { RowIcon(R.drawable.ic_font_size) },
          onClick = { showLyricSizeDialog = true },
        )
        Spacer(Modifier.height(Space.xs))
        NavigationRow(
          title = stringResource(R.string.settings_translation),
          subtitle = translationEntries[if (state.translation == Translation.CH) 1 else 0],
          leading = { RowIcon(R.drawable.ic_toggle_translation) },
          onClick = { showTranslationDialog = true },
        )
      }

      SectionLabel(stringResource(R.string.settings_data))
      SettingsCard {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Text(
            text = stringResource(R.string.settings_reset_stats),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f),
          )
          Surface(
            shape = PillShape,
            color = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.clickable { showResetConfirm = true },
          ) {
            Text(
              text = stringResource(R.string.settings_reset),
              style = MaterialTheme.typography.labelMedium,
              modifier = Modifier.padding(horizontal = Space.sm, vertical = Space.xs),
            )
          }
        }

        Spacer(Modifier.height(Space.xs))
        NavigationRow(
          title = stringResource(R.string.settings_backup),
          subtitle = stringResource(R.string.settings_backup_subtitle),
          leading = { RowIcon(R.drawable.ic_upload) },
          onClick = { onEvent(SettingsEvent.Export) },
        )
        NavigationRow(
          title = stringResource(R.string.settings_restore),
          subtitle = if (isImporting) stringResource(R.string.settings_restoring)
          else stringResource(R.string.settings_restore_subtitle),
          leading = { RowIcon(R.drawable.ic_import) },
          trailing = {
            if (isImporting) CircularProgressIndicator(modifier = Modifier.size(20.dp))
          },
          onClick = { if (!isImporting) jsonPicker.launch("application/json") },
        )
      }

      SectionLabel(stringResource(R.string.settings_support))
      SettingsCard {
        NavigationRow(
          title = stringResource(R.string.settings_feedback),
          subtitle = stringResource(R.string.feedback),
          leading = { RowIcon(R.drawable.ic_whatsapp) },
          onClick = {
            onEvent(SettingsEvent.Analytics.Feedback)
            context.startActivity(Intent(ACTION_VIEW).setData(parse(whatsAppUrl)))
          },
        )
        NavigationRow(
          title = stringResource(R.string.settings_rate),
          subtitle = stringResource(R.string.rate),
          leading = { RowIcon(R.drawable.ic_rate) },
          onClick = {
            onEvent(SettingsEvent.Analytics.Rating)
            context.startActivity(Intent(ACTION_VIEW).setData(parse(playStoreUrl)))
          },
        )
      }

      val version = getAppVersion(context)?.name ?: "—"
      Text(
        text = stringResource(R.string.about_version, version),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = Space.lg),
      )
    }
  }

  if (showFontDialog) {
    FontSelectionDialog(
      currentFontName = state.fontFamily,
      availableFonts = FontManager.Font.entries.map { it.font },
      onDismiss = { showFontDialog = false },
      onSelect = { onEvent(SettingsEvent.FontStyle.Selected(it)) },
    )
  }

  if (showThemeDialog) {
    RadioSelectDialog(
      title = stringResource(R.string.settings_theme),
      options = listOf(
        stringResource(R.string.settings_theme_system),
        stringResource(R.string.settings_theme_light),
        stringResource(R.string.settings_theme_dark),
      ),
      selectedIndex = when (state.appTheme) {
        AppTheme.SYSTEM -> 0
        AppTheme.LIGHT -> 1
        AppTheme.DARK -> 2
      },
      onSelect = { index ->
        onEvent(
          SettingsEvent.ThemeMode(
            when (index) {
              1 -> AppTheme.LIGHT
              2 -> AppTheme.DARK
              else -> AppTheme.SYSTEM
            },
          ),
        )
      },
      onDismiss = { showThemeDialog = false },
    )
  }

  if (showLyricSizeDialog) {
    RadioSelectDialog(
      title = stringResource(R.string.settings_lyric_size),
      options = listOf(
        stringResource(R.string.settings_size_small),
        stringResource(R.string.settings_size_medium),
        stringResource(R.string.settings_size_large),
      ),
      selectedIndex = when (LyricSizePreset.nearest(state.fontSize)) {
        LyricSizePreset.SMALL -> 0
        LyricSizePreset.LARGE -> 2
        else -> 1
      },
      onSelect = { index ->
        onEvent(
          SettingsEvent.LyricSize(
            when (index) {
              0 -> LyricSizePreset.SMALL
              2 -> LyricSizePreset.LARGE
              else -> LyricSizePreset.MEDIUM
            },
          ),
        )
      },
      onDismiss = { showLyricSizeDialog = false },
    )
  }

  if (showTranslationDialog) {
    RadioSelectDialog(
      title = stringResource(R.string.settings_translation),
      options = translationEntries,
      selectedIndex = if (state.translation == Translation.CH) 1 else 0,
      onSelect = { index ->
        onEvent(
          SettingsEvent.ChangeTranslation(
            if (index == 1) Translation.CH else Translation.EN,
          ),
        )
      },
      onDismiss = { showTranslationDialog = false },
    )
  }

  if (showResetConfirm) {
    AlertDialog(
      onDismissRequest = { showResetConfirm = false },
      title = { Text(stringResource(R.string.settings_reset_confirm_title)) },
      text = { Text(stringResource(R.string.settings_reset_confirm_message)) },
      confirmButton = {
        TextButton(
          onClick = {
            onEvent(SettingsEvent.ResetListeningStats)
            showResetConfirm = false
            context.toast("Listening stats reset")
          },
        ) { Text(stringResource(R.string.settings_reset)) }
      },
      dismissButton = {
        TextButton(onClick = { showResetConfirm = false }) {
          Text(stringResource(R.string.settings_cancel))
        }
      },
    )
  }
}

/** Leading icon for a settings row, drawn from the app's own drawable set. */
@Composable
private fun RowIcon(@DrawableRes id: Int) {
  Icon(
    painter = painterResource(id),
    contentDescription = null,
    tint = MaterialTheme.colorScheme.primary,
    modifier = Modifier.size(Sizes.icon),
  )
}

@Composable
private fun SectionLabel(title: String) {
  Text(
    text = title.uppercase(),
    style = MaterialTheme.typography.labelMedium,
    color = MaterialTheme.colorScheme.primary,
    modifier = Modifier.padding(top = Space.md, bottom = Space.xs),
  )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
  Surface(
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surfaceContainer,
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column(modifier = Modifier.padding(Space.sm)) { content() }
  }
}

/**
 * A single-choice list in a dialog, one radio button per option. Replaces the
 * horizontal segmented rows previously used for Theme, Lyric size and
 * Translation — those squeezed three-option labels into a fixed-width row and
 * didn't scale to more options; a vertical radio list (the same modal pattern
 * as font selection) reads clearly regardless of label length or option count.
 * Selecting an option applies it immediately and closes the dialog.
 */
@Composable
private fun RadioSelectDialog(
  title: String,
  options: List<String>,
  selectedIndex: Int,
  onSelect: (Int) -> Unit,
  onDismiss: () -> Unit,
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text(title) },
    text = {
      Column {
        options.forEachIndexed { index, label ->
          val selected = index == selectedIndex
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clip(MaterialTheme.shapes.small)
              .clickable {
                onSelect(index)
                onDismiss()
              }
              .padding(vertical = Space.xxs),
          ) {
            RadioButton(selected = selected, onClick = null)
            Text(
              text = label,
              style = MaterialTheme.typography.bodyLarge,
              modifier = Modifier.padding(start = Space.xs),
            )
          }
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) { Text(stringResource(R.string.action_close)) }
    },
  )
}

@Composable
private fun FontSelectionDialog(
  currentFontName: String,
  availableFonts: List<String>,
  onDismiss: () -> Unit,
  onSelect: (String) -> Unit,
) {
  var previewFont by remember { mutableStateOf(currentFontName) }
  val context = LocalContext.current

  AlertDialog(
    properties = DialogProperties(usePlatformDefaultWidth = false),
    modifier = Modifier.fillMaxWidth(0.9f),
    onDismissRequest = onDismiss,
    title = { Text(stringResource(R.string.settings_choose_font)) },
    text = {
      Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Text(
          text = stringResource(R.string.settings_font_preview),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(bottom = Space.xs),
        )
        Card(
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
          ),
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Space.md),
        ) {
          Text(
            text = stringResource(R.string.font_pangram),
            style = MaterialTheme.typography.titleMedium.copy(
              fontFamily = FontManager.getFontFamilyFromName(previewFont, context),
            ),
            modifier = Modifier.padding(Space.sm),
          )
        }

        Text(
          text = stringResource(R.string.settings_font_available),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(bottom = Space.xs),
        )
        availableFonts.forEach { fontName ->
          val fontFamily = FontManager.getFontFamilyFromName(fontName, context)
          val selected = fontName == previewFont
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .clickable { previewFont = fontName }
              .padding(vertical = Space.xs),
          ) {
            Text(
              text = "Aa",
              style = MaterialTheme.typography.titleLarge.copy(fontFamily = fontFamily),
              color = if (selected) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.padding(end = Space.md),
            )
            Column(modifier = Modifier.weight(1f)) {
              Text(text = fontName, style = MaterialTheme.typography.bodyMedium)
              Text(
                text = FontManager.description(fontName),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            if (selected) {
              Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
              )
            }
          }
        }
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onSelect(previewFont)
          onDismiss()
        },
      ) { Text(stringResource(R.string.settings_apply)) }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) { Text(stringResource(R.string.settings_cancel)) }
    },
  )
}
