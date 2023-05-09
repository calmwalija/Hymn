package net.techandgraphics.hymn.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.databinding.FragmentMainBinding

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private val viewModel by viewModels<MainViewModel>()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(FragmentMainBinding.bind(view)) {
      lyricAdapter = MainAdapter {
        MainFragmentDirections
          .actionLyricFragmentToReadFragment(it)
          .apply {
            findNavController().navigate(this)
          }
      }.apply {
        viewModel.lyric.asLiveData().observe(viewLifecycleOwner) {
          submitData(viewLifecycleOwner.lifecycle, it)
        }
      }
    }
    onRestart()
  }

  private fun onRestart() = with(requireActivity().intent) {
    if (getBooleanExtra(Constant.RESTART, false)) {
      Utils.toast(requireContext(), "Version changed.")
    }
  }
}
