package net.techandgraphics.hymn.presentation.fragments.discover

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentDiscoverBinding

@AndroidEntryPoint
class DiscoverFragment : Fragment(R.layout.fragment_discover) {

  private val viewModel: DiscoverViewModel by viewModels()
  private lateinit var adapter: DiscoverAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(FragmentDiscoverBinding.bind(view)) {
      adapter = DiscoverAdapter {
        DiscoverFragmentDirections
          .actionDiscoverFragmentToCategoryFragment(it)
          .apply { findNavController().navigate(this) }
      }.also { it.stateRestorationPolicy() }
      viewModel.discover.onEach {
        adapter.submitList(it)
      }.launchIn(viewModel.viewModelScope)
      adapterRv = adapter
      Tag.screenView(viewModel.firebaseAnalytics, Tag.DISCOVER)
    }
  }
}
