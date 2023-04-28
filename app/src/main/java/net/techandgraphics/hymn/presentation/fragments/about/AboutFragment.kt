package net.techandgraphics.hymn.presentation.fragments.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.databinding.FragmentAboutBinding
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {

  private lateinit var bind: FragmentAboutBinding
  private val viewModel: BaseViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    bind = FragmentAboutBinding.bind(view)

    bind.tag.setOnClickListener {
      Utils.openWebsite(requireActivity(), "https://techandgraphics.net")
    }

    bind.zonse.setOnClickListener {
      Utils.openWebsite(requireActivity(), "https://zonse.live")
    }

    Tag.screenView(viewModel.firebaseAnalytics, Tag.ABOUT)
  }
}
