package net.techandgraphics.hymn.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils.onAddTextChangedListener
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.data.local.entities.Lyric
import net.techandgraphics.hymn.data.local.entities.Search
import net.techandgraphics.hymn.databinding.FragmentSearchBinding
import net.techandgraphics.hymn.presentation.BaseViewModel
import net.techandgraphics.hymn.presentation.fragments.search.SearchRandomAdapterEvent.OnClick

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

  private lateinit var binding: FragmentSearchBinding
  private val viewModel by viewModels<BaseViewModel>()
  private lateinit var searchAdapter: SearchAdapter
  private lateinit var randomAdapter: SearchRandomAdapter
  private lateinit var searchTagAdapter: SearchTagAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentSearchBinding.bind(view)
    binding.searchEt.requestFocus()

    searchAdapter = SearchAdapter(click = {
      val searchQuery = binding.searchEt.text.toString().trim().lowercase()
      val searchList = searchQuery.regexLowerCase().split(" ")

      if (searchQuery.isNotBlank())
        Search(
          query = searchQuery,
          tag = buildString { searchList.forEach { append(it) } }
        ).also { viewModel.insert(it) }

      viewModel.firebaseAnalytics.logEvent(
        Tag.KEYWORD,
        bundleOf(Pair(Tag.KEYWORD, searchQuery))
      )

      actionToReadFragment(it)

    },
      favorite = {
        viewModel.update(it.copy(favorite = !it.favorite))
      }).also { it.stateRestorationPolicy() }

    searchTagAdapter = SearchTagAdapter {
      binding.searchEt.setText(it.query)
      binding.searchEt.setSelection(it.query.length)
    }.also { it.stateRestorationPolicy() }
    with(binding) {
      searchEt.onAddTextChangedListener {
        recyclerViewAll.isVisible = it.isBlank().not()
        searchIcon.isInvisible = it.isBlank().not()
        animationView.isInvisible = it.isBlank()
        emptyRoom.isVisible = it.isBlank()
        clearText.isVisible = it.isNotBlank()
        viewModel.searchQuery.value = it
      }
    }


    viewModel.searchInput.onEach {
      binding.searchIcon.isInvisible = false
      binding.animationView.isInvisible = true
    }.launchIn(viewLifecycleOwner.lifecycleScope)


    viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
      searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
    }

    viewModel.observeSearch().observe(viewLifecycleOwner) {
      searchTagAdapter.submitList(it)
    }

    randomAdapter = SearchRandomAdapter {
      when (it) {
        is OnClick -> actionToReadFragment(it.lyric)
      }
    }

    viewModel.queryRandom.onEach {
      randomAdapter.submitList(it)
    }.launchIn(viewLifecycleOwner.lifecycleScope)

    binding.clearText.setOnClickListener { binding.searchEt.text = null }
    binding.favoriteAdapter = searchAdapter
    binding.searchAdapter = searchTagAdapter
    binding.randomAdapter = randomAdapter
    Tag.screenView(viewModel.firebaseAnalytics, Tag.SEARCH)

  }

  private fun actionToReadFragment(lyric: Lyric) {
    SearchFragmentDirections.actionSearchFragmentToReadFragment(lyric).apply {
      findNavController().navigate(this)
    }
  }
}