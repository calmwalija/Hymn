package net.techandgraphics.hymn.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentSearchBinding
import net.techandgraphics.hymn.models.Search
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.ui.fragments.main.MainAdapter
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.onAddTextChangedListener
import net.techandgraphics.hymn.utils.Utils.regexLowerCase
import net.techandgraphics.hymn.utils.Utils.share
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel by viewModels<BaseViewModel>()
    private lateinit var mainAdapter: MainAdapter
    private lateinit var searchTagAdapter: SearchTagAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentSearchBinding.bind(view)
        binding.searchEt.requestFocus()

        mainAdapter = MainAdapter(itemClickListener = {
            SearchFragmentDirections.actionSearchFragmentToReadFragment(it).apply {
                findNavController().navigate(this)
            }

            val searchQuery = binding.searchEt.text.toString().trim()
            val searchList = searchQuery.regexLowerCase().split(" ")

            if (searchQuery.isNotBlank())
                Search(
                    query = searchQuery,
                    tag = buildString { searchList.forEach { append(it) } }
                ).also {
                    viewModel.insert(it)
                }
        },
            share = {
                Utils.createDynamicLink(requireParentFragment(), it)
            }).also { it.stateRestorationPolicy() }

        searchTagAdapter = SearchTagAdapter({
            binding.searchEt.setText(it.query)
            binding.searchEt.setSelection(it.query.length)
        },
            { viewModel.delete(it) },
            { viewModel.delete(it) }
        ).also { it.stateRestorationPolicy() }

        binding.searchEt.onAddTextChangedListener {
            viewModel.searchQuery.value = it
        }

        viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        viewModel.observeSearch().observe(viewLifecycleOwner) {
            searchTagAdapter.submitList(it)
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.mainAdapter = mainAdapter
        binding.searchAdapter = searchTagAdapter

    }
}