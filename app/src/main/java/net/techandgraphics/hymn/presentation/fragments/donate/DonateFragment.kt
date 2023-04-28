package net.techandgraphics.hymn.presentation.fragments.donate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.databinding.FragmentDonateBinding
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class DonateFragment : Fragment(R.layout.fragment_donate) {

  private val viewModel: BaseViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    FragmentDonateBinding.bind(view).apply {

      paypal.setOnClickListener {
        Utils.openWebsite(requireActivity(), "https://paypal.me/samlungu")
      }

      nb.apply {
        setOnClickListener {
          Utils.toast(context, "Text copied to clipboard.")
          (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(ClipData.newPlainText("", account.text))
        }
      }

      Tag.screenView(viewModel.firebaseAnalytics, Tag.ABOUT)
    }
  }
}
