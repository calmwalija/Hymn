package net.techandgraphics.hymn.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.firebase.Tag
import net.techandgraphics.hymn.firebase.tagScreen
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
  private val categoryRepo: CategoryRepository,
  private val analytics: FirebaseAnalytics
) : ViewModel() {

  private val _state = MutableStateFlow(CategoryUiState())
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
}
