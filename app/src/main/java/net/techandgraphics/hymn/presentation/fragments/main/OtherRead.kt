package net.techandgraphics.hymn.presentation.fragments.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentMainOtherReadBinding

@AndroidEntryPoint
class OtherRead : Fragment(R.layout.fragment_main_other_read) {

    private lateinit var binding: FragmentMainOtherReadBinding
    private val args: OtherReadArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentMainOtherReadBinding.bind(view)
        binding.other = args.other


        val fontSize =
            PreferenceManager.getDefaultSharedPreferences(requireContext())
                .getInt(getString(R.string.font_key), 1)

        binding.appCompatTextView.textSize = (fontSize.plus(14)).toFloat()

        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            with(supportActionBar!!) {
                setHomeButtonEnabled(true)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                title = args.other.groupName
            }
        }

    }
}