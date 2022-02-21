package net.techandgraphics.hymn.ui.fragments.main

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentMainBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.prefs.UserPrefs
import net.techandgraphics.hymn.ui.adapters.LyricAdapter
import net.techandgraphics.hymn.ui.adapters.RecentAdapter
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Constant
import net.techandgraphics.hymn.utils.Tag
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.dialog
import net.techandgraphics.hymn.utils.Utils.dialogShow
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding
    private lateinit var lyricAdapter: LyricAdapter
    private lateinit var recentAdapter: RecentAdapter
    private val viewModel by viewModels<BaseViewModel>()
    private lateinit var dialog: Dialog

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
        dialog = Dialog(requireContext()).dialog()
        lyricAdapter =
            LyricAdapter(click = {
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
            lyricAdapter.submitList(it)
        }


        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
            binding.recent.isVisible = it.isEmpty().not() && it.size > 3
        }


        with(binding) {
            recyclerViewAll.adapter = lyricAdapter
            recyclerViewRecent.adapter = recentAdapter
            recyclerViewAll.itemAnimator = null

            search.setOnClickListener {
                viewModel.firebaseAnalytics.logEvent(
                    Tag.SEARCH_BTN,
                    bundleOf(Pair(Tag.SEARCH_BTN, null))
                )
                MainFragmentDirections.actionMainFragmentToSearchFragment().also {
                    findNavController().navigate(it)
                }
            }

        }
        onRestart()

        setupDynamicLink()

        userPrefs.getWhatsNew.asLiveData().observe(viewLifecycleOwner) {
            if (it) {
                with(dialog) {
                    setContentView(R.layout.dialog)
                    setCancelable(false)
                    findViewById<View>(R.id.tryButton).setOnClickListener {
                        dismiss()
                        viewModel.viewModelScope.launch { userPrefs.setWhatsNew(false) }
                        MainFragmentDirections.actionMainFragmentToSettingsFragment().also {
                            findNavController().navigate(it)
                        }
                    }
                    dialogShow()
                }
            }
        }

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