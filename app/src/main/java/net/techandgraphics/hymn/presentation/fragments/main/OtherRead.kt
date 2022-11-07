package net.techandgraphics.hymn.presentation.fragments.main

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.databinding.FragmentMainOtherReadBinding

@AndroidEntryPoint
class OtherRead : Fragment(R.layout.fragment_main_other_read) {

  private lateinit var binding: FragmentMainOtherReadBinding
  private val args: OtherReadArgs by navArgs()
  private var fontSize = 2


  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.read_menu, menu)
    menu.getItem(0).isVisible = false
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.font_size) {
      Utils.changeFontSize(Dialog(requireContext()), fontSize) {
        binding.appCompatTextView.textSize =
          it.plus(14).toFloat().also {
            fontSize = it.minus(14).toInt()
          }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentMainOtherReadBinding.bind(view)
    binding.other = args.other

    setHasOptionsMenu(true)

    fontSize =
      PreferenceManager.getDefaultSharedPreferences(requireContext())
        .getInt(getString(R.string.font_key), 2)

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