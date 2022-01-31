package net.techandgraphics.hymn.ui.fragments.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
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
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.ui.fragments.LyricAdapter
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main), PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var lyricAdapter: LyricAdapter
    private lateinit var recentAdapter: MainRecentAdapter
    private val viewModel by viewModels<BaseViewModel>()

    @Inject
    lateinit var userPrefs: UserPrefs

    companion object {
        enum class SortBy {
            NUMBER, NAME, CATEGORY
        }
    }

    private fun setupDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(requireActivity().intent)
            .addOnSuccessListener(requireActivity()) { pendingDynamicLink ->
                if (pendingDynamicLink != null)
                    pendingDynamicLink.link?.let {
                        it.getQueryParameter("id")?.toInt()?.let {
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
            LyricAdapter(click = {
                it.navigateToReadFragment()
            },
                share = {
                    Utils.createDynamicLink(requireParentFragment(), it)
                }).also { it.stateRestorationPolicy() }

        recentAdapter =
            MainRecentAdapter { it.navigateToReadFragment() }.also { it.stateRestorationPolicy() }

        userPrefs.getSort.asLiveData().observe(viewLifecycleOwner) { name ->
            viewModel.observeSortBy(name).observe(viewLifecycleOwner) {
                lyricAdapter.submitList(it) {
                    binding.recyclerViewAll.scrollToPosition(0)
                }
            }
        }

        viewModel.observeRecentLyrics().observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
        }


        with(binding) {
            recyclerViewAll.adapter = lyricAdapter
            recyclerViewRecent.adapter = recentAdapter
            recyclerViewAll.itemAnimator = null

            sortBy.setOnClickListener {
                val popupMenu = PopupMenu(requireContext(), it)
                popupMenu.inflate(R.menu.sort_by_menu)
                popupMenu.setOnMenuItemClickListener(this@MainFragment)
                userPrefs.getSort.asLiveData().observe(viewLifecycleOwner) { name ->
                    when (name) {
                        SortBy.CATEGORY.name ->
                            popupMenu.menu.getItem(0).subMenu.getItem(2).isChecked = true
                        SortBy.NAME.name ->
                            popupMenu.menu.getItem(0).subMenu.getItem(1).isChecked = true
                        SortBy.NUMBER.name ->
                            popupMenu.menu.getItem(0).subMenu.getItem(0).isChecked = true
                    }
                }
                popupMenu.show()
            }
        }

        setupDynamicLink()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.sortByName -> {
                item.isChecked = true
                viewModel.viewModelScope.launch { userPrefs.setSort(SortBy.NAME.name) }
                true
            }
            R.id.sortByNumber -> {
                item.isChecked = true
                viewModel.viewModelScope.launch { userPrefs.setSort(SortBy.NUMBER.name) }
                true
            }
            R.id.sortByCategory -> {
                item.isChecked = true
                viewModel.viewModelScope.launch { userPrefs.setSort(SortBy.CATEGORY.name) }
                true
            }
            else -> false
        }
    }


}