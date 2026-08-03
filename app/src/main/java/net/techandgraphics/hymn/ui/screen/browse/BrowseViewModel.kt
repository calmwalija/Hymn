package net.techandgraphics.hymn.ui.screen.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.domain.repository.CategoryRepository
import net.techandgraphics.hymn.domain.repository.LyricRepository
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class BrowseViewModel @Inject constructor(
  private val lyricRepo: LyricRepository,
  private val categoryRepo: CategoryRepository,
) : ViewModel() {

  private val query = MutableStateFlow("")
  private val sort = MutableStateFlow(BrowseSort.Number)
  private val favoritesOnly = MutableStateFlow(false)

  val state = combine(query, sort, favoritesOnly) { q, s, fav ->
    Triple(q, s, fav)
  }.flatMapLatest { (q, s, fav) ->
    flow {
      when (s) {
        BrowseSort.Categories -> {
          emit(
            BrowseUiState(
              query = q,
              sort = s,
              favoritesOnly = fav,
              categories = categoryRepo.query(q),
            )
          )
        }

        BrowseSort.Title -> {
          lyricRepo.queryByTitle(q).collect { hymns ->
            emit(
              BrowseUiState(
                query = q,
                sort = s,
                favoritesOnly = fav,
                hymns = hymns.filter { !fav || it.favorite },
              )
            )
          }
        }

        BrowseSort.Number -> {
          lyricRepo.query(q).collect { hymns ->
            emit(
              BrowseUiState(
                query = q,
                sort = s,
                favoritesOnly = fav,
                hymns = hymns.filter { !fav || it.favorite },
              )
            )
          }
        }
      }
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BrowseUiState())

  fun onEvent(event: BrowseUiEvent) {
    when (event) {
      is BrowseUiEvent.Query -> query.update { event.value }
      is BrowseUiEvent.Sort -> sort.update { event.sort }
      BrowseUiEvent.ToggleFavoritesOnly -> favoritesOnly.update { !it }
      is BrowseUiEvent.Favorite -> viewModelScope.launch {
        lyricRepo.favorite(event.favorite, event.number)
      }
      is BrowseUiEvent.OpenHymn, is BrowseUiEvent.OpenCategory -> Unit
    }
  }
}
