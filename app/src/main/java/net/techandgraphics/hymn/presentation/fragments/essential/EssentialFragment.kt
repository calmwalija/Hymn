package net.techandgraphics.hymn.presentation.fragments.essential

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.databinding.FragmentEssentialBinding

@AndroidEntryPoint
class EssentialFragment : Fragment(R.layout.fragment_essential) {

  private lateinit var binding: FragmentEssentialBinding
  private val args: EssentialFragmentArgs by navArgs()
  private val viewModel by viewModels<EssentialViewModel>()

  private var fontSize = 2

  companion object {
    private const val FONT_THRESHOLD = 14
    private const val DEFAULT_FONT_SIZE = 2
  }

  private fun addMenuProvider() = requireActivity().addMenuProvider(
    object : MenuProvider {
      override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.read_menu, menu)
        menu.getItem(0).isVisible = false
      }

      override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
          R.id.font_size -> {
            Utils.changeFontSize(Dialog(requireContext()), fontSize) {
              binding.appCompatTextView.textSize =
                it.plus(FONT_THRESHOLD).toFloat().also {
                  fontSize = it.minus(FONT_THRESHOLD).toInt()
                }
            }
            true
          }
          else -> false
        }
      }
    },
    viewLifecycleOwner, Lifecycle.State.RESUMED
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(FragmentEssentialBinding.bind(view)) {
      essential = args.essential
      binding = this
      addMenuProvider()
      viewModel.firebaseAnalytics(args.essential)
      fontSize =
        PreferenceManager.getDefaultSharedPreferences(requireContext())
          .getInt(getString(R.string.font_key), DEFAULT_FONT_SIZE)
      appCompatTextView.textSize = (fontSize.plus(FONT_THRESHOLD)).toFloat()
      (requireActivity() as AppCompatActivity).apply {
        setSupportActionBar(toolbar)
        with(supportActionBar!!) {
          setHomeButtonEnabled(true)
          setDisplayHomeAsUpEnabled(true)
          setHomeAsUpIndicator(R.drawable.ic_arrow_back)
          title = args.essential.groupName
        }
      }
    }
  }
}
