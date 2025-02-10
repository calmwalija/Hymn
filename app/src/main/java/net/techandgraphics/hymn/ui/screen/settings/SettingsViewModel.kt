package net.techandgraphics.hymn.ui.screen.settings

import android.net.Uri
import android.util.Log
import androidx.compose.ui.text.font.FontFamily
import androidx.core.os.bundleOf
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
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.fontFile
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.settings.SettingsChannelEvent.Import
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
    analytics.tagScreen(Tag.MISC_SCREEN)
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
    channel.send(Import.Import(Import.Status.Wait))
    runCatching {
      val jsonString = getFile(uri).bufferedReader().use { it.readText() }
      Gson().fromJson(jsonString, ExportData::class.java)
    }.onSuccess { import ->

      if (import == null || import.currentTimeMillis.hash(import.toHash()) != import.hashable) {
        channel.send(Import.Import(Import.Status.Invalid))
        return@launch
      }

      val total = import.run { search.size + favorites.size + timestamp.size + timeSpent.size }
      var currentProgress = 0

      try {

        import.search.forEach {
          searchRepo.upsert(listOf(SearchEntity(it.query, it.tag, it.lang)))
          currentProgress += 1
          channel.send(Import.Progress(Import.ProgressStatus(total, currentProgress)))
        }

        import.timestamp.forEach {
          currentProgress += 1
          if (lyricRepo.queryByNumber(number = it.number).first().timestamp < it.timestamp)
            lyricRepo.read(it.number, it.timestamp, it.lang)

          timestampRepo.import(it)
          channel.send(Import.Progress(Import.ProgressStatus(total, currentProgress)))
        }

        import.timeSpent.forEach {
          if (timeSpentRepo.getCount(it.toEntity()) == 0)
            timeSpentRepo.upsert(listOf(it.toEntity()))
          currentProgress += 1
          channel.send(Import.Progress(Import.ProgressStatus(total, currentProgress)))
        }

        import.favorites.forEach {
          currentProgress += 1
          lyricRepo.favorite(true, it)
          channel.send(Import.Progress(Import.ProgressStatus(total, currentProgress)))
        }

        channel.send(Import.Import(Import.Status.Success))
      } catch (e: Exception) {
        channel.send(Import.Import(Import.Status.Invalid))
      }
    }.onFailure {
      channel.send(Import.Import(Import.Status.Error))
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

  fun onEvent(event: SettingsUiEvent) {
    when (event) {
      is SettingsUiEvent.RemoveFav -> {
        analytics.tagEvent(
          if (event.data.favorite) Tag.ADD_FAVORITE else Tag.REMOVE_FAV,
          bundleOf(Pair(Tag.MISC_SCREEN, event.data.title))
        )
        favorite(event.data)
      }

      is SettingsUiEvent.OpenCreed -> {
        analytics.tagEvent(
          Tag.OPEN_CREED,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is SettingsUiEvent.OpenFavorite -> {
        analytics.tagEvent(
          Tag.OPEN_FAVORITE,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is SettingsUiEvent.OpenFeedback -> {
        analytics.tagEvent(
          Tag.OPEN_FEEDBACK,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is SettingsUiEvent.OpenLordsPrayer -> {
        analytics.tagEvent(
          Tag.OPEN_LORDS_PRAYER,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is SettingsUiEvent.OpenRating -> {
        analytics.tagEvent(
          Tag.OPEN_RATING,
          bundleOf(Pair(Tag.MISC_SCREEN, state.value.lang.lowercase()))
        )
      }

      is SettingsUiEvent.Import -> onImport(event.uri)
      SettingsUiEvent.Export -> onExport()
      is SettingsUiEvent.DynamicColor -> onDynamicColor(event.isEnabled)
      SettingsUiEvent.Font.Default -> onFontDefault()
      is SettingsUiEvent.Font.Selected -> onFontSelected(event.fontFamily, event.fontName)

      else -> Unit
    }
  }

  private fun onFontDefault() = viewModelScope.launch {
    runCatching { prefs.context.fontFile().delete() }
    _state.update { it.copy(fontFamily = null) }
    prefs.remove(stringPreferencesKey(prefs.fontStyleKey))
    channel.send(SettingsChannelEvent.FontStyle(null))
  }

  private fun onFontSelected(fontFamily: FontFamily?, fontName: String?) = viewModelScope.launch {
    if (fontFamily == null) {
      onFontDefault()
      prefs.context.toast("The selected font is invalid. Please choose a valid font and try again.")
      return@launch
    }

    prefs.put(prefs.fontStyleKey, fontName)
    _state.update { it.copy(fontFamily = fontName) }
    channel.send(SettingsChannelEvent.FontStyle(fontFamily))
  }

  private fun onDynamicColor(isEnabled: Boolean) = viewModelScope.launch {
    prefs.put(prefs.dynamicColorKey, isEnabled)
    _state.update {
      it.copy(dynamicColor = prefs.get<Boolean>(prefs.dynamicColorKey, true) ?: true)
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
    }
}
