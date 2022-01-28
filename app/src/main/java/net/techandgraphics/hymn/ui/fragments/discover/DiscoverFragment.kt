package net.techandgraphics.hymn.ui.fragments.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentDiscoverBinding
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy

@AndroidEntryPoint
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var binding: FragmentDiscoverBinding
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var browseAdapter: DiscoverBrowseAdapter
    private lateinit var topPickAdapter: DiscoverTopPickAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDiscoverBinding.bind(view)


        topPickAdapter = DiscoverTopPickAdapter {
            DiscoverFragmentDirections.actionDiscoverFragmentToReadFragment(it).apply {
                findNavController().navigate(this)
            }
        }.also { it.stateRestorationPolicy() }


        browseAdapter = DiscoverBrowseAdapter {
            DiscoverFragmentDirections.actionDiscoverFragmentToCategoryFragment(it).apply {
                findNavController().navigate(this)
            }
        }.also { it.stateRestorationPolicy() }

        viewModel.observeCategories().observe(viewLifecycleOwner) {
            browseAdapter.submitList(it)
        }

        viewModel.observeTopPickCategories().observe(viewLifecycleOwner) {
            topPickAdapter.submitList(it)
        }


        binding.recyclerViewBrowseCategory.adapter = browseAdapter
        binding.recyclerViewTopPick.adapter = topPickAdapter

    }
}