package net.techandgraphics.hymn.ui.screen.settings

import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.font.FontFamily
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.local.entities.TimeSpentEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.dateFormat
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.combined
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.fontFile
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import.Import
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import.Progress
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import.ProgressStatus
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import.Status
import net.techandgraphics.hymn.ui.screen.settings.SettingsEvent.Analytics
import net.techandgraphics.hymn.ui.screen.settings.SettingsEvent.DynamicColor
import net.techandgraphics.hymn.ui.screen.settings.SettingsEvent.Export
import net.techandgraphics.hymn.ui.screen.settings.SettingsEvent.FontStyle
import net.techandgraphics.hymn.ui.screen.settings.export.ExportData
import net.techandgraphics.hymn.ui.screen.settings.export.hash
import net.techandgraphics.hymn.ui.screen.settings.export.toHash
import net.techandgraphics.hymn.ui.screen.settings.export.write
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val timeSpentRepo: TimeSpentRepository,
  private val timestampRepo: TimestampRepository,
  private val searchRepo: SearchRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics,
) : ViewModel() {

  private val _state = MutableStateFlow(SettingsUiState())
  val state = _state.asStateFlow()

  private val channel = Channel<SettingsChannelEvent>()
  val channelFlow = channel.receiveAsFlow()

  init {
    analytics.tagScreen(Tag.SETTINGS_SCREEN)
    viewModelScope.launch {
      onQuery()
      _state.update {
        it.copy(
          dynamicColor = prefs.get<Boolean>(prefs.dynamicColorKey, true) ?: true,
          fontFamily = prefs.get<String?>(prefs.fontStyleKey, null)
        )
      }
      lyricRepo.favorites().onEach { favorites ->
        _state.value = _state.value.copy(
          favorites = favorites,
          fontSize = prefs.get(prefs.fontKey, 1.toString()).toInt()
        )
      }.launchIn(this)
    }
  }

  private fun stressTest() {
    Log.e("TAG", " ====================== stressTest Insert Started ======================")

    lyricRepo.query("").onEach { lyrics ->
      lyrics.forEachIndexed { index, lyric ->

        Log.e(
          "TAG",
          " ====================== stressTest at $index  of ${lyrics.size} Done ======================"
        )

        repeat(Random.nextInt(5, 20)) {

          val timeSpent = TimeSpentEntity(
            number = lyric.number,
            timeSpent = Random.nextLong(30000, 900000),
            lang = lyric.lang
          )
          timeSpentRepo.run { upsert(listOf((timeSpent))) }
        }
      }
    }.launchIn(viewModelScope)

    Log.e("TAG", " ====================== stressTest Insert Done ======================")
  }

  private fun getFile(uri: Uri): File {
    val tempFile = File(prefs.context.cacheDir, "HymnImport.json")
    prefs.context.contentResolver.openInputStream(uri)?.use { input ->
      tempFile.outputStream().use { output ->
        input.copyTo(output)
      }
    }
    return tempFile
  }

  private fun onImport(uri: Uri) = viewModelScope.launch {
    channel.send(Import(Status.Wait))
    runCatching {
      val jsonString = getFile(uri).bufferedReader().use { it.readText() }
      Gson().fromJson(jsonString, ExportData::class.java)
    }.onSuccess { import ->

      if (import == null || import.currentTimeMillis.hash(import.toHash()) != import.hashable) {
        channel.send(Import(Status.Invalid))
        return@launch
      }

      val total = import.run { search.size + favorites.size + timestamp.size + timeSpent.size }
      var currentProgress = 0

      try {

        import.search.forEach {
          searchRepo.upsert(listOf(SearchEntity(it.query, it.tag, it.lang)))
          currentProgress += 1
          channel.send(Progress(ProgressStatus(total, currentProgress)))
        }

        import.timestamp.forEach {
          currentProgress += 1
          if (lyricRepo.queryByNumber(number = it.number).first().timestamp < it.timestamp)
            lyricRepo.read(it.number, it.timestamp, it.lang)

          timestampRepo.import(it)
          channel.send(Progress(ProgressStatus(total, currentProgress)))
        }

        import.timeSpent.forEach {
          if (timeSpentRepo.getCount(it.toEntity()) == 0)
            timeSpentRepo.upsert(listOf(it.toEntity()))
          currentProgress += 1
          channel.send(Progress(ProgressStatus(total, currentProgress)))
        }

        import.favorites.forEach {
          currentProgress += 1
          lyricRepo.favorite(true, it)
          channel.send(Progress(ProgressStatus(total, currentProgress)))
        }

        channel.send(Import(Status.Success))
      } catch (e: Exception) {
        channel.send(Import(Status.Invalid))
      }
      onAnalytics(Analytics.ImportData(Status.Success.name, uri.path.toString()))
    }.onFailure {
      onAnalytics(Analytics.ImportData(Status.Error.name, uri.path.toString()))
      channel.send(Import(Status.Error))
    }
  }

  private fun onExport() {
    viewModelScope.launch(Dispatchers.IO) {
      val currentTimeMillis = System.currentTimeMillis()
      val fileName = "Hymn Book BackUp ${dateFormat(currentTimeMillis)}.json"
      val toExportData = ExportData(
        currentTimeMillis = currentTimeMillis,
        favorites = state.value.favExport,
        timeSpent = state.value.timeSpentExport,
        timestamp = state.value.timeStampExport,
        search = state.value.searchExport,
      )
      val hashable = currentTimeMillis.hash(toExportData.toHash())
      val jsonToExport = Gson().toJson(toExportData.copy(hashable = hashable))
      val file = prefs.context.write(jsonToExport, fileName)
      onAnalytics(Analytics.ExportData(currentTimeMillis))
      channel.send(SettingsChannelEvent.Export.Export(file))
    }
  }

  private suspend fun onQuery() {
    _state.update {
      it.copy(
        timeSpentExport = timeSpentRepo.toExport(),
        timeStampExport = timestampRepo.toExport(),
        searchExport = searchRepo.toExport(),
        favExport = lyricRepo.toExport()
      )
    }
  }

  fun onEvent(event: SettingsEvent) {
    when (event) {
      is SettingsEvent.Import -> onImport(event.uri)
      is DynamicColor -> onDynamicColor(event.isEnabled)
      is FontStyle -> onFont(event)
      is Analytics -> onAnalytics(event)
      Export -> onExport()
    }
  }

  private fun onAnalytics(event: Analytics) {
    when (event) {
      is Analytics.Feedback ->
        analytics.tagEvent(Tag.OPEN_FEEDBACK, Pair(Tag.OPEN_FEEDBACK, state.value.lang))

      is Analytics.Rating ->
        analytics.tagEvent(Tag.OPEN_RATING, Pair(Tag.OPEN_RATING, state.value.lang))

      is Analytics.AppFontStyle ->
        analytics.tagEvent(Tag.APP_FONT_STYLE, Pair(Tag.APP_FONT_STYLE, event.fontFamily))

      is Analytics.ExportData ->
        analytics.combined(
          Tag.EXPORT_DATA,
          Pair(Tag.EXPORT_DATA_DATE, dateFormat(event.timestamp)),
          Pair(Tag.EXPORT_DATA_TIMESTAMP, event.timestamp),
        )

      is Analytics.ImportData ->
        analytics.combined(
          Tag.IMPORT_DATA,
          Pair(Tag.IMPORT_DATA_FILE, event.fileName),
          Pair(Tag.IMPORT_DATA_STATUS, event.status),
        )

      is Analytics.ThemeColor ->
        analytics.tagEvent(Tag.THEME_COLOR, Pair(Tag.THEME_COLOR, event.isEnabled))
    }
  }

  private fun onFont(event: FontStyle) {
    when (event) {
      FontStyle.Default -> onFontDefault()
      is FontStyle.Selected -> onFontSelected(event.fontFamily, event.fontName)
      else -> Unit
    }
  }

  private fun onFontDefault() = viewModelScope.launch {
    runCatching { prefs.context.fontFile().delete() }
    _state.update { it.copy(fontFamily = null) }
    onAnalytics(Analytics.AppFontStyle(FontFamily.Default.toString()))
    prefs.remove(stringPreferencesKey(prefs.fontStyleKey))
    channel.send(SettingsChannelEvent.FontStyle(null))
  }

  private fun onFontSelected(fontFamily: FontFamily?, fontName: String?) = viewModelScope.launch {
    if (fontFamily == null) {
      onFontDefault()
      prefs.context.toast("The selected font is invalid. Please choose a valid font and try again.")
      return@launch
    }
    fontName?.let { onAnalytics(Analytics.AppFontStyle(it)) }
    prefs.put(prefs.fontStyleKey, fontName)
    _state.update { it.copy(fontFamily = fontName) }
    channel.send(SettingsChannelEvent.FontStyle(fontFamily))
  }

  private fun onDynamicColor(isEnabled: Boolean) = viewModelScope.launch {
    prefs.put(prefs.dynamicColorKey, isEnabled)
    onAnalytics(Analytics.ThemeColor(isEnabled))
    _state.update {
      it.copy(dynamicColor = prefs.get<Boolean>(prefs.dynamicColorKey, true) ?: true)
    }
  }
}
