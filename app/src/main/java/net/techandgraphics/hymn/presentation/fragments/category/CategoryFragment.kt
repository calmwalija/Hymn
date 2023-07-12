package net.techandgraphics.hymn.presentation.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentCategoryBinding
import net.techandgraphics.hymn.domain.model.Lyric

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

  private lateinit var binding: FragmentCategoryBinding
  private val viewModel: CategoryViewModel by viewModels()
  private val args: CategoryFragmentArgs by navArgs()
  private lateinit var categoryAdapter: CategoryAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentCategoryBinding.bind(view)
    binding.lyric = args.discover.lyric

    categoryAdapter = CategoryAdapter {
      when (it) {
        is CategoryAdapter.CategoryEvent.Click ->
          CategoryFragmentDirections
            .actionCategoryFragmentToReadFragment(it.lyric).apply {
              findNavController().navigate(this)
            }

        is CategoryAdapter.CategoryEvent.Favorite -> {
          viewModel.update(it.lyric)
          toast(it.lyric)
        }
      }
    }.also { it.stateRestorationPolicy() }

    viewModel.category(args.discover.lyric).onEach {
      binding.counter = it.size
      categoryAdapter.submitList(it)
    }.launchIn(lifecycleScope)

    with(binding.recyclerView) {
      setHasFixedSize(true)
      adapter = categoryAdapter
      itemAnimator = null
    }
    viewModel.firebaseAnalytics()
  }

  private fun toast(lyric: Lyric) =
    requireContext().apply {
      Utils.toast(
        this,
        if (lyric.favorite.not()) getString(R.string.add_favorite, lyric.number) else
          getString(R.string.remove_favorite, lyric.number)
      )
    }
}
