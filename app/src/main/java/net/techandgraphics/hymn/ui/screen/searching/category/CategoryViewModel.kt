package net.techandgraphics.hymn.ui.screen.searching.category

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagEvent
import net.techandgraphics.hymn.firebase.tagScreen
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
  private val categoryRepo: CategoryRepository,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(CategoryState())
  val state = _state.asStateFlow()
  private val delayDuration = 3L
  private var searchJob: Job? = null

  init {
    analytics.tagScreen(Tag.CATEGORY_SCREEN)
    get()
  }

  private fun get() = categoryRepo.query(_state.value.searchQuery)
    .onEach { _state.value = _state.value.copy(categories = it) }
    .launchIn(viewModelScope)

  private fun clear() {
    viewModelScope.launch {
      delay(delayDuration)
      state.value.searchQuery.length.let {
        try {
          _state.value =
            _state.value.copy(searchQuery = state.value.searchQuery.dropLast(1))
          if (it > 1) clear()
        } catch (_: Exception) {
        }
      }
      onEvent(CategoryEvent.OnSearchQuery(state.value.searchQuery))
    }
  }

  fun onEvent(event: CategoryEvent) {
    when (event) {
      is CategoryEvent.OnSearchQuery -> {
        _state.update { it.copy(searchQuery = event.searchQuery, isSearching = true) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
          delay(300)
          get()
          delay(delayDuration.times(300))
          _state.update { it.copy(isSearching = false) }
        }
      }

      CategoryEvent.ClearSearchQuery -> {
        analytics.tagEvent(Tag.CLEAR_CATEGORY_SEARCH_TAG, bundleOf())
        clear()
      }

      else -> Unit
    }
  }
}
