package net.techandgraphics.hymn.ui.fragments.about

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentAboutBinding
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Tag
import net.techandgraphics.hymn.utils.Utils

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


        bind.paypal.setOnClickListener {
            Utils.openWebsite(requireActivity(), "https://paypal.me/samlungu")
        }


        bind.nb.apply {
            setOnClickListener {
                Utils.toast(context, "Text copied to clipboard.")
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(ClipData.newPlainText("", text))
            }
        }

        Tag.screenView(viewModel.firebaseAnalytics, Tag.ABOUT)

    }
}