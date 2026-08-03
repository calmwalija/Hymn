package net.techandgraphics.hymn.ui.screen.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.model.CategoryAffinity
import net.techandgraphics.hymn.domain.model.HymnStat
import net.techandgraphics.hymn.domain.model.InsightsSummary
import net.techandgraphics.hymn.domain.repository.InsightsRepository
import javax.inject.Inject

data class InsightsUiState(
  val loading: Boolean = true,
  val thisYearOnly: Boolean = false,
  val summary: InsightsSummary = InsightsSummary(0, 0, 0, 0, 0),
  val mostVisited: List<HymnStat> = emptyList(),
  val topByTime: List<HymnStat> = emptyList(),
  val themes: List<CategoryAffinity> = emptyList(),
)

@HiltViewModel
class InsightsViewModel @Inject constructor(
  private val insightsRepo: InsightsRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(InsightsUiState())
  val state = _state.asStateFlow()

  init {
    refresh()
  }

  fun toggleYear() {
    _state.update { it.copy(thisYearOnly = !it.thisYearOnly) }
    refresh()
  }

  private fun refresh() = viewModelScope.launch {
    _state.update { it.copy(loading = true) }
    val year = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val from = if (_state.value.thisYearOnly) {
      java.util.Calendar.getInstance().apply {
        clear()
        set(java.util.Calendar.YEAR, year)
        set(java.util.Calendar.MONTH, java.util.Calendar.JANUARY)
        set(java.util.Calendar.DAY_OF_MONTH, 1)
      }.timeInMillis
    } else {
      0L
    }
    val to = if (_state.value.thisYearOnly) {
      java.util.Calendar.getInstance().apply {
        clear()
        set(java.util.Calendar.YEAR, year + 1)
        set(java.util.Calendar.MONTH, java.util.Calendar.JANUARY)
        set(java.util.Calendar.DAY_OF_MONTH, 1)
      }.timeInMillis
    } else {
      0L
    }
    _state.update {
      it.copy(
        loading = false,
        summary = insightsRepo.summary(from, to),
        mostVisited = insightsRepo.mostVisited(5, from, to),
        topByTime = insightsRepo.topByTime(5, from, to),
        themes = insightsRepo.topCategories(5, from, to),
      )
    }
  }
}
