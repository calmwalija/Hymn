package net.techandgraphics.hymn.presentation.fragments.read

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.changeFontSize
import net.techandgraphics.hymn.Utils.dialog
import net.techandgraphics.hymn.Utils.dialogShow
import net.techandgraphics.hymn.databinding.FragmentReadBinding
import net.techandgraphics.hymn.domain.model.Lyric

@AndroidEntryPoint
class ReadFragment : Fragment(R.layout.fragment_read) {

  private val args: ReadFragmentArgs by navArgs()
  private lateinit var readAdapter: ReadAdapter
  private val viewModel: ReadViewModel by viewModels()
  private lateinit var menu: Menu
  private lateinit var lyric: Lyric
  private var fontSize = 2
  private lateinit var sharedPrefs: SharedPreferences
  private lateinit var inverseVersion: String
  private lateinit var versionValue: Array<String>
  private var fontThreshold = 18

  private fun addMenuProvider() = requireActivity()
    .addMenuProvider(
      object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
          menuInflater.inflate(R.menu.read_menu, menu)
          this@ReadFragment.menu = menu
          favorite(args.lyric)
          inverseHymn(args.lyric)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
          return when (menuItem.itemId) {
            R.id.favorite -> {
              lyric = lyric.copy(favorite = !lyric.favorite, timestamp = lyric.timestamp)
              viewModel.update(lyric)
              favorite(lyric)

              if (lyric.favorite) {
                viewModel.firebaseAnalytics.logEvent(
                  Tag.ADD_FAVORITE,
                  bundleOf(Pair(Tag.ADD_FAVORITE, lyric.number))
                )
                requireContext().apply {
                  Utils.toast(this, getString(R.string.add_favorite, lyric.number))
                }
              } else {
                requireContext().apply {
                  Utils.toast(this, getString(R.string.remove_favorite, lyric.number))
                }
                viewModel.firebaseAnalytics.logEvent(
                  Tag.REMOVE_FAV,
                  bundleOf(Pair(Tag.REMOVE_FAV, lyric.number))
                )
              }
              true
            }

            R.id.font_size -> {
              changeFontSizeDialog()
              true
            }

            R.id.bookSwitch -> {
              inverseHymnBottomSheetDialog(inverseVersion)
              true
            }

            else -> false
          }
        }
      },
      viewLifecycleOwner, Lifecycle.State.RESUMED
    )

  @SuppressLint("NotifyDataSetChanged")
  private fun changeFontSizeDialog() {
    changeFontSize(Dialog(requireContext()), fontSize) {
      readAdapter.fontSize = it.plus(fontThreshold).also {
        fontSize = it.minus(fontThreshold)
      }
      readAdapter.notifyDataSetChanged()
    }
  }

  private fun Lyric.topPickHit() {
    viewModel.topPickHit(
      copy(
        topPickHit = args.lyric.topPickHit.plus(1),
        timestamp = System.currentTimeMillis()
      )
    )
  }

  private fun inverseHymn(lyric: Lyric) {
    menu.getItem(1).icon = ContextCompat.getDrawable(
      requireContext(),
      if (lyric.lang == versionValue.last()) R.drawable.ic_book_en_menu else R.drawable.ic_book_ch_menu
    )
    viewModel.getInverseLyricsById(inverseVersion, args.lyric).onEach {
      menu.getItem(1).isVisible = it.isEmpty().not()
    }.launchIn(lifecycleScope)
  }

  private fun inverseHymnBottomSheetDialog(version: String) {
    BottomSheetDialog(requireContext()).dialog().apply {
      setContentView(R.layout.dialog_inverse_hymn)
      findViewById<View>(R.id.closeButton).setOnClickListener { dismiss() }
      val title = findViewById<AppCompatTextView>(R.id.title)
      findViewById<RecyclerView>(R.id.recyclerView).apply {
        adapter = ReadAdapter(fontSize.plus(fontThreshold)).also { adapter ->
          viewModel.getInverseLyricsById(version, args.lyric).onEach {
            title.text = it.firstOrNull()?.title ?: ""
            adapter.submitList(it)
          }.launchIn(lifecycleScope)
        }
      }
      dialogShow()
    }
  }

  private fun favorite(lyric: Lyric) {
    menu.getItem(0).icon = ContextCompat.getDrawable(
      requireContext(),
      if (lyric.favorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(FragmentReadBinding.bind(view)) {
      this@ReadFragment.lyric = args.lyric
      sharedPrefs = PreferenceManager
        .getDefaultSharedPreferences(requireContext())
      fontSize = sharedPrefs.getInt(getString(R.string.font_key), 2)
      versionValue = requireActivity().resources.getStringArray(R.array.version_values)
      val oldValue =
        sharedPrefs.getString(getString(R.string.version_key), versionValue.first())
      inverseVersion =
        if (oldValue == versionValue.last()) versionValue.first() else versionValue.last()
      readAdapter = ReadAdapter(fontSize + fontThreshold).also { adapter = it }
      viewModel.lyric(args.lyric)
        .onEach {
          lyric = it[0]
          readAdapter.submitList(it)
        }.launchIn(lifecycleScope)
      addMenuProvider()
      setupToolbar(this)
      args.lyric.topPickHit()
      recyclerView.itemAnimator = null
      recyclerView.setHasFixedSize(true)
      viewModel.firebaseAnalytics(args.lyric)
      fabFont.setOnClickListener { changeFontSizeDialog() }
    }
  }

  private fun setupToolbar(bind: FragmentReadBinding) {
    (requireActivity() as AppCompatActivity).apply {
      setSupportActionBar(bind.toolbar)
      with(supportActionBar!!) {
        setHomeButtonEnabled(true)
        setDisplayHomeAsUpEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = lyric.title
      }
    }
  }
}
