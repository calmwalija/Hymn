package net.techandgraphics.hymn.ui.screen.settings

import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.data.local.entities.SearchEntity
import net.techandgraphics.hymn.data.prefs.DataStorePrefs
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.repository.LyricRepository
import net.techandgraphics.hymn.domain.repository.OtherRepository
import net.techandgraphics.hymn.domain.repository.SearchRepository
import net.techandgraphics.hymn.domain.repository.TimeSpentRepository
import net.techandgraphics.hymn.domain.repository.TimestampRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import net.techandgraphics.hymn.ui.screen.settings.export.ExportData
import net.techandgraphics.hymn.ui.screen.settings.export.hash
import net.techandgraphics.hymn.ui.screen.settings.export.toHash
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val otherRepo: OtherRepository,
  private val timeSpentRepo: TimeSpentRepository,
  private val timestampRepo: TimestampRepository,
  private val searchRepo: SearchRepository,
  private val prefs: DataStorePrefs,
  private val analytics: FirebaseAnalytics,
) : ViewModel() {

  private val _state = MutableStateFlow(SettingsUiState())
  val state = _state.asStateFlow()

  init {
    analytics.tagScreen(Tag.MISC_SCREEN)
    viewModelScope.launch {
      onQuery()
      lyricRepo.favorites().onEach { favorites ->
        _state.value = _state.value.copy(
          favorites = favorites,
          complementary = otherRepo.query(),
          fontSize = prefs.get(prefs.fontKey, 1.toString()).toInt()
        )
      }.launchIn(this)
    }
  }

  private fun onImport(file: File) {
    runCatching {
      val jsonString = file.bufferedReader().use { it.readText() }
      Gson().fromJson(jsonString, ExportData::class.java)
    }.onSuccess { import ->
      if (import.currentTimeMillis.hash(import.toHash()) == import.hashable) {
        viewModelScope.launch {
          searchRepo.upsert(import.search.map { SearchEntity(it.query, it.tag, it.lang) })
          timestampRepo.import(import.timestamp)
          timeSpentRepo.upsert(import.timeSpent.map { it.toEntity() })
          import.favorites.forEach { lyricRepo.favorite(true, it) }
          import.timestamp.forEach { lyricRepo.read(it.number, it.timestamp, it.lang) }
        }

        Log.e("TAG", "onImport: we are inside")
      }
    }.onFailure {
      Log.e("TAG", "onReadJson: ", it)
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

      is SettingsUiEvent.Import -> onImport(event.file)
    }
  }

  private fun favorite(lyric: Lyric) =
    viewModelScope.launch {
      with(lyric.copy(favorite = !lyric.favorite)) {
        lyricRepo.favorite(favorite, number)
      }
    }
}
