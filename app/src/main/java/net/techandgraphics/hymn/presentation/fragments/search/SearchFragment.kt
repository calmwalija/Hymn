package net.techandgraphics.hymn.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.onAddTextChangedListener
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentSearchBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.tagEvent

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

  private lateinit var binding: FragmentSearchBinding
  private val viewModel by viewModels<SearchViewModel>()
  private lateinit var searchAdapter: SearchAdapter
  private lateinit var searchTagAdapter: SearchTagAdapter
  private val delayDuration = 3L

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentSearchBinding.bind(view)
    binding.searchEt.requestFocus()
    searchAdapter = SearchAdapter {
      when (it) {
        is SearchAdapterOnEvent.Delete -> {
          viewModel.forTheService(it.lyric)
          Utils.toast(
            requireContext(),
            requireActivity().getString(R.string.add_for_the_service, it.lyric.number)
          )
        }

        is SearchAdapterOnEvent.Click -> {
          val searchQuery = binding.searchEt.text.toString().trim().lowercase()
          val searchList = searchQuery.regexLowerCase().split(" ")
          if (searchQuery.isNotBlank())
            Search(
              query = searchQuery,
              tag = buildString { searchList.forEach { append(it) } },
            ).also { viewModel.insert(it) }
          viewModel.firebaseAnalytics.tagEvent(
            Tag.KEYWORD,
            bundleOf(Pair(Tag.KEYWORD, searchQuery))
          )
          actionToReadFragment(it.lyric)
        }
      }
    }.also { it.stateRestorationPolicy() }

    searchTagAdapter = SearchTagAdapter { appendSearchText(it) }
      .also { it.stateRestorationPolicy() }
    with(binding) {
      searchEt.onAddTextChangedListener {
        searchIcon.isInvisible = it.isBlank().not()
        animationView.isInvisible = it.isBlank()
        emptyRoom.isVisible = it.isBlank()
        clearText.isVisible = it.isNotBlank()
        viewModel.searchQuery.value = it.trim()
      }
    }

    viewModel.searchInput.onEach {
      binding.searchIcon.isInvisible = false
      binding.animationView.isInvisible = true
    }.launchIn(lifecycleScope)

    viewModel.lyric.onEach {
      searchAdapter.submitData(lifecycle, it)
    }.launchIn(lifecycleScope)

    viewModel.tag.onEach {
      searchTagAdapter.submitList(it)
    }.launchIn(lifecycleScope)

    binding.clearText.setOnClickListener { clearSearchText() }
    binding.favoriteAdapter = searchAdapter
    binding.searchAdapter = searchTagAdapter
    viewModel.firebaseAnalyticsScreen()
    Tag.screenView(viewModel.firebaseAnalytics, Tag.SEARCH)
  }

  private fun actionToReadFragment(lyric: Lyric) {
    SearchFragmentDirections.actionSearchFragmentToReadFragment(lyric).apply {
      findNavController().navigate(this)
    }
  }

  private fun clearSearchText() {
    viewModel.firebaseAnalytics.tagEvent(Tag.CLEAR_SEARCH_TAG, bundleOf())
    viewModel.viewModelScope.launch {
      val text = binding.searchEt.text
      delay(delayDuration)
      text?.length?.let {
        try {
          text.delete(it.minus(1), it)
          if (it > 1) clearSearchText()
        } catch (_: Exception) {
        }
      }
    }
  }

  private fun appendSearchText(search: Search) = with(binding.searchEt) {
    viewModel.firebaseAnalytics.tagEvent(Tag.APPEND_SEARCH_TAG, bundleOf())
    viewModel.viewModelScope.launch {
      search.query.onEachIndexed { index, char ->
        delay(delayDuration)
        try {
          append(char.toString())
          setSelection(index.plus(1))
        } catch (_: Exception) {
        }
      }
    }
  }
}
