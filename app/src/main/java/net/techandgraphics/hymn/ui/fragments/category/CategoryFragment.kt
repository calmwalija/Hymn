package net.techandgraphics.hymn.ui.fragments.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentCategoryBinding
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Utils
import net.techandgraphics.hymn.utils.Utils.share
import net.techandgraphics.hymn.utils.Utils.stateRestorationPolicy


@AndroidEntryPoint
class CategoryFragment : Fragment(R.layout.fragment_category) {

    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: BaseViewModel by viewModels()
    private val args: CategoryFragmentArgs by navArgs()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentCategoryBinding.bind(view)
        binding.lyric = args.lyric


        categoryAdapter = CategoryAdapter(itemClickListener = {
            CategoryFragmentDirections.actionCategoryFragmentToReadFragment(it).apply {
                findNavController().navigate(this)
            }
        }, share = {
            Utils.createDynamicLink(requireParentFragment(), it)
        }).also { it.stateRestorationPolicy() }

        viewModel.getLyricsByCategory(args.lyric).observe(viewLifecycleOwner) {
            binding.counter = it.size
            categoryAdapter.submitList(it)
        }



        with(binding.recyclerView) {
            setHasFixedSize(true)
            adapter = categoryAdapter
        }


    }
}