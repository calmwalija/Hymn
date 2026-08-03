package net.techandgraphics.hymn.ui.screen.wrapped

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.model.YearInHymnsReport
import net.techandgraphics.hymn.domain.repository.InsightsRepository
import javax.inject.Inject

data class WrappedUiState(
  val loading: Boolean = true,
  val report: YearInHymnsReport? = null,
)

@HiltViewModel
class WrappedViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val insightsRepo: InsightsRepository,
) : ViewModel() {

  private val year: Int = savedStateHandle["year"] ?: java.util.Calendar.getInstance()
    .get(java.util.Calendar.YEAR)

  private val _state = MutableStateFlow(WrappedUiState())
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch {
      _state.update { it.copy(loading = false, report = insightsRepo.yearReport(year)) }
    }
  }
}
