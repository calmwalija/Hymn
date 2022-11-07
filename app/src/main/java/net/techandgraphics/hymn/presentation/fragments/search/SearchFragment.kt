package net.techandgraphics.hymn.presentation.fragments.search

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils.onAddTextChangedListener
import net.techandgraphics.hymn.Utils.regexLowerCase
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentSearchBinding
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

  private lateinit var binding: FragmentSearchBinding
  private val viewModel by viewModels<BaseViewModel>()
  private lateinit var searchAdapter: SearchAdapter
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
        ).also {
          viewModel.insert(it)
        }

      viewModel.firebaseAnalytics.logEvent(
        Tag.KEYWORD,
        bundleOf(Pair(Tag.KEYWORD, searchQuery))
      )


      SearchFragmentDirections.actionSearchFragmentToReadFragment(it).apply {
        findNavController().navigate(this)
      }
    },
      favorite = {
        viewModel.update(it.copy(favorite = !it.favorite))
      }).also { it.stateRestorationPolicy() }

    searchTagAdapter = SearchTagAdapter({
      binding.searchEt.setText(it.query)
      binding.searchEt.setSelection(it.query.length)
    },
      { viewModel.delete(it) },
      { viewModel.delete(it) }
    ).also { it.stateRestorationPolicy() }

    binding.searchEt.onAddTextChangedListener {
      binding.recyclerViewAll.isVisible = it.isBlank().not()
      binding.animationView.isVisible = it.isBlank()
      viewModel.searchQuery.value = it
    }

    viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
      searchAdapter.submitData(viewLifecycleOwner.lifecycle, it)
    }

    viewModel.observeSearch().observe(viewLifecycleOwner) {
      searchTagAdapter.submitList(it)
    }

    binding.recyclerView.layoutManager =
      LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    binding.favoriteAdapter = searchAdapter
    binding.searchAdapter = searchTagAdapter
    Tag.screenView(viewModel.firebaseAnalytics, Tag.SEARCH)

  }
}