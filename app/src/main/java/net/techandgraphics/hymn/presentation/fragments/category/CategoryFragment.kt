package net.techandgraphics.hymn.presentation.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentCategoryBinding
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

  private lateinit var binding: FragmentCategoryBinding
  private val viewModel: BaseViewModel by viewModels()
  private val args: CategoryFragmentArgs by navArgs()
  private lateinit var categoryAdapter: CategoryAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentCategoryBinding.bind(view)
    binding.lyric = args.lyric.lyric

    categoryAdapter = CategoryAdapter(
      click = {
        CategoryFragmentDirections.actionCategoryFragmentToReadFragment(it).apply {
          findNavController().navigate(this)
        }
      },
      favorite = {
        viewModel.update(it.copy(favorite = !it.favorite))
        requireContext().apply {
          Utils.toast(
            this,
            if (it.favorite.not()) getString(R.string.add_favorite, it.number) else
              getString(R.string.remove_favorite, it.number)
          )
        }
      }
    ).also { it.stateRestorationPolicy() }

    viewModel.getLyricsByCategory(args.lyric.lyric).observe(viewLifecycleOwner) {
      binding.counter = it.size
      categoryAdapter.submitList(it)
    }

    with(binding.recyclerView) {
      setHasFixedSize(true)
      adapter = categoryAdapter
      itemAnimator = null
    }

    Tag.screenView(viewModel.firebaseAnalytics, Tag.CATEGORY)
  }
}
