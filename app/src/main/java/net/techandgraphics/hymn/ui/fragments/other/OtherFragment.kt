package net.techandgraphics.hymn.ui.fragments.other

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentOtherBinding
import net.techandgraphics.hymn.ui.fragments.BaseViewModel

@AndroidEntryPoint
class OtherFragment : Fragment(R.layout.fragment_other) {

    private lateinit var binding: FragmentOtherBinding
    private val viewModel by viewModels<BaseViewModel>()
    private lateinit var adapter: OtherAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding = FragmentOtherBinding.bind(view)
        val fontSize =
            PreferenceManager.getDefaultSharedPreferences(requireContext()).getInt("font", 1)
        adapter = OtherAdapter(fontSize + 14)
        binding.adapter = adapter

        viewModel.observeOther().observe(viewLifecycleOwner) { adapter.submitList(it) }

    }
}