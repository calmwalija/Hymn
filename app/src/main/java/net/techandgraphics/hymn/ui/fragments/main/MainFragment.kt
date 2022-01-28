package net.techandgraphics.hymn.ui.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentMainBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var mainAdapter: MainAdapter
    private lateinit var recentAdapter: MainRecentAdapter
    private val viewModel by viewModels<BaseViewModel>()


    private fun Lyric.navigateToReadFragment() =
        MainFragmentDirections.actionLyricFragmentToReadFragment(this).apply {
            findNavController().navigate(this)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)

        mainAdapter =
            MainAdapter(itemClickListener = {
                it.navigateToReadFragment()
            },
                share = {
                    Utils.createDynamicLink(requireParentFragment(), it)
                }).also { it.stateRestorationPolicy() }

        recentAdapter =
            MainRecentAdapter { it.navigateToReadFragment() }.also { it.stateRestorationPolicy() }


        viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
            mainAdapter.submitList(it)
        }

        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
        }


        with(binding) {
            recyclerViewAll.setHasFixedSize(true)
            recyclerViewAll.adapter = mainAdapter
            recyclerViewRecent.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            recyclerViewRecent.adapter = recentAdapter
        }

    }
}