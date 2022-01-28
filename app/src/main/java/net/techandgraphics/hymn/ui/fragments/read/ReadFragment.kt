package net.techandgraphics.hymn.ui.fragments.read

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentReadBinding
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Constant
import net.techandgraphics.hymn.utils.Utils

@AndroidEntryPoint
class ReadFragment : Fragment(R.layout.fragment_read) {

    private val args: ReadFragmentArgs by navArgs()
    private lateinit var readAdapter: ReadAdapter
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var binding: FragmentReadBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentReadBinding.bind(view)
        binding.lyric = args.lyric

        readAdapter = ReadAdapter().also { binding.adapter = it }


        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            with(supportActionBar!!) {
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }

        val bitmap = Utils.decodeResource(
            requireContext(),
            Constant.images[args.lyric.categoryId].drawableRes
        )

        Utils.createPaletteSync(bitmap).apply {

            val dominantColor = getVibrantColor(
                ContextCompat.getColor(requireContext(), R.color.mellon)
            )

            requireActivity().window.statusBarColor = dominantColor

            if (dominantColor == -2200468) {
                getDominantColor(ContextCompat.getColor(requireContext(), R.color.mellon)).also {
                    requireActivity().window.statusBarColor = it
                }
            }
        }

        binding.fabShare.setOnClickListener {
            Utils.createDynamicLink(requireParentFragment(), args.lyric)
        }

        viewModel.getLyricsById(args.lyric).observe(viewLifecycleOwner) {
            readAdapter.submitList(it)
        }

        viewModel.update(args.lyric)
        binding.recyclerView.setHasFixedSize(true)
    }
}