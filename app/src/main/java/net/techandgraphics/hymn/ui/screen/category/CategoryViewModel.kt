package net.techandgraphics.hymn.ui.screen.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
  categoryRepo: CategoryRepository
) : ViewModel() {

  private val _state = MutableStateFlow(CategoryState())
  val state = _state.asStateFlow()

  init {
    categoryRepo.query().onEach { _state.value = _state.value.copy(categories = it) }
      .launchIn(viewModelScope)
  }
}
