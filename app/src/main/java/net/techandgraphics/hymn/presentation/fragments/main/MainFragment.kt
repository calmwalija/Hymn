package net.techandgraphics.hymn.presentation.fragments.main

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.dialog
import net.techandgraphics.hymn.Utils.dialogShow
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentMainBinding
import net.techandgraphics.hymn.data.local.entities.Lyric
import net.techandgraphics.hymn.data.prefs.UserPrefs
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private lateinit var bind: FragmentMainBinding
  private val viewModel by viewModels<BaseViewModel>()
  private lateinit var dialog: Dialog


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
    bind = FragmentMainBinding.bind(view)
    bind.lyricAdapter = MainAdapter(click = {
      it.navigateToReadFragment()
    },
      share = {
        Utils.createDynamicLink(
          requireParentFragment(),
          it,
          viewModel.firebaseAnalytics
        )
      }).apply {
      stateRestorationPolicy()
      viewModel.observeHymnLyrics().observe(viewLifecycleOwner) {
        submitData(viewLifecycleOwner.lifecycle, it)
      }
    }

    bind.otherAdapter = OtherAdapter {
      MainFragmentDirections.actionMainFragmentToOtherFragment(it).apply {
        findNavController().navigate(this)
      }
    }.apply {
      viewModel.observeOther().observe(viewLifecycleOwner) {
        submitList(it)
      }
    }

    with(bind) {
      recyclerViewAll.itemAnimator = null
    }
    onRestart()
    setupDynamicLink()
    whatsNew()
  }

  private fun whatsNew() {
    viewModel.userPrefs.getWhatsNew.onEach {
      if (it != UserPrefs.WHATS_NEW) {
        viewModel.userPrefs.whatsNew(UserPrefs.WHATS_NEW)
        dialog = Dialog(requireContext()).dialog()
        dialog.apply {
          setContentView(R.layout.dialog)
          findViewById<View>(R.id.closeButton).setOnClickListener { dismiss() }
          dialogShow()
        }
      }
    }.launchIn(viewLifecycleOwner.lifecycleScope)

  }

  private fun onRestart() = with(requireActivity().intent) {
    if (getBooleanExtra(Constant.RESTART, false)) {
      if (getBooleanExtra(Constant.RESTART, false)) {
        data = null
        replaceExtras(Bundle())
        Utils.toast(requireContext(), "Version changed.")
      }
    }
  }
}