package net.techandgraphics.hymn.presentation.fragments.discover

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentDiscoverBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.BaseViewModel
import net.techandgraphics.hymn.presentation.adapters.RecentAdapter
import net.techandgraphics.hymn.presentation.fragments.main.MainFragmentDirections

@AndroidEntryPoint
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

    private lateinit var binding: FragmentDiscoverBinding
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var browseAdapter: DiscoverBrowseAdapter
    private lateinit var recentAdapter: RecentAdapter


    private fun Lyric.navigateToReadFragment() =
        DiscoverFragmentDirections.actionDiscoverFragmentToReadFragment(this).apply {
            findNavController().navigate(this)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentDiscoverBinding.bind(view)


        recentAdapter =
            RecentAdapter { it.navigateToReadFragment() }.also { it.stateRestorationPolicy() }


        browseAdapter = DiscoverBrowseAdapter {
            DiscoverFragmentDirections.actionDiscoverFragmentToCategoryFragment(it).apply {
                findNavController().navigate(this)
            }
        }.also { it.stateRestorationPolicy() }

        viewModel.observeCategories().observe(viewLifecycleOwner) {
            browseAdapter.submitList(it)
        }

        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
            binding.recent.isVisible = it.isEmpty().not() && it.size > 3
        }

        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
            binding.recent.isVisible = it.isEmpty().not() && it.size > 3
        }

        binding.recyclerViewBrowseCategory.adapter = browseAdapter
        binding.recyclerViewTopPick.adapter = recentAdapter
        Tag.screenView(viewModel.firebaseAnalytics, Tag.DISCOVER)

    }
}