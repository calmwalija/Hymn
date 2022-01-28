package net.techandgraphics.hymn.ui.fragments.about

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.databinding.FragmentAboutBinding
import net.techandgraphics.hymn.utils.Utils

class AboutFragment : Fragment(R.layout.fragment_about) {

    private lateinit var bind: FragmentAboutBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bind = FragmentAboutBinding.bind(view)

        bind.tag.setOnClickListener {
            requireActivity().startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://techandgraphics.net"))
            )
        }


        bind.zonse.setOnClickListener {
            requireActivity().startActivity(
                Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://zonse.live"))
            )
        }


        bind.nb.apply {
            setOnClickListener {
                Utils.toast(context, "Text copied to clipboard.")
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                    .setPrimaryClip(ClipData.newPlainText("", text))
            }
        }
    }
}