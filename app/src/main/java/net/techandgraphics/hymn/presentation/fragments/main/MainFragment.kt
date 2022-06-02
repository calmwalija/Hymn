package net.techandgraphics.hymn.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.databinding.FragmentMainBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.BaseViewModel
import net.techandgraphics.hymn.presentation.adapters.RecentAdapter
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var lyricAdapter: MainAdapter
    private lateinit var recentAdapter: RecentAdapter
    private val viewModel by viewModels<BaseViewModel>()

    @Inject
    lateinit var userPrefs: UserPrefs


    private fun setupDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(requireActivity().intent)
            .addOnSuccessListener(requireActivity()) { pendingDynamicLink ->
                if (pendingDynamicLink != null)
                    pendingDynamicLink.link?.let {
                        it.getQueryParameter("id")?.toInt()?.let {
                            viewModel.firebaseAnalytics.logEvent(
                                Tag.LINK_OPEN, bundleOf(Pair(Tag.LINK_OPEN, it))
                            )
                            viewModel.findLyricById(it).observe(viewLifecycleOwner) {
                                MainFragmentDirections.actionLyricFragmentToReadFragment(it).also {
                                    requireActivity().intent.data = null
                                    requireActivity().intent.replaceExtras(Bundle())
                                    findNavController().navigate(it)
                                }
                            }
                        }
                    }
            }
    }

    private fun Lyric.navigateToReadFragment() =
        MainFragmentDirections.actionLyricFragmentToReadFragment(this).apply {
            findNavController().navigate(this)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainBinding.bind(view)
        lyricAdapter =
            MainAdapter(click = {
                it.navigateToReadFragment()
            },
                share = {
                    Utils.createDynamicLink(
                        requireParentFragment(),
                        it,
                        viewModel.firebaseAnalytics
                    )
                }).also { it.stateRestorationPolicy() }

        recentAdapter =
            RecentAdapter { it.navigateToReadFragment() }.also { it.stateRestorationPolicy() }

        viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
            lyricAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }


        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
            binding.recent.isVisible = it.isEmpty().not() && it.size > 3
        }


        with(binding) {
            recyclerViewAll.adapter = lyricAdapter
            recyclerViewRecent.adapter = recentAdapter
            recyclerViewAll.itemAnimator = null

        }
        onRestart()

        setupDynamicLink()
    }

    private fun onRestart() {
        if (requireActivity().intent.getBooleanExtra(Constant.RESTART, false)) {
            MainFragmentDirections
                .actionMainFragmentToSettingsFragment().also {
                    findNavController().navigate(it)
                }
        }
    }
}