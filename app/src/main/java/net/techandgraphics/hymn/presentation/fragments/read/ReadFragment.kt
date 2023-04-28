package net.techandgraphics.hymn.presentation.fragments.read

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.changeFontSize
import net.techandgraphics.hymn.data.local.entities.Lyric
import net.techandgraphics.hymn.databinding.FragmentReadBinding
import net.techandgraphics.hymn.presentation.BaseViewModel

@AndroidEntryPoint
class ReadFragment : Fragment(R.layout.fragment_read) {

  private val args: ReadFragmentArgs by navArgs()
  private lateinit var readAdapter: ReadAdapter
  private val viewModel: BaseViewModel by viewModels()
  private lateinit var binding: FragmentReadBinding
  private lateinit var menu: Menu
  private lateinit var lyric: Lyric
  private var fontSize = 2

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.read_menu, menu)
    this.menu = menu
    favorite(args.lyric)
  }

  private fun favorite(lyric: Lyric) {
    menu.getItem(0).icon = ContextCompat.getDrawable(
      requireContext(),
      if (lyric.favorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite
    )
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
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
      }
      R.id.font_size -> {
        changeFontSize(Dialog(requireContext()), fontSize) {
          readAdapter.fontSize = it.plus(14).also {
            fontSize = it.minus(14)
          }
          readAdapter.notifyDataSetChanged()
        }
      }
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding = FragmentReadBinding.bind(view)
    lyric = args.lyric
    setHasOptionsMenu(true)

    fontSize =
      PreferenceManager.getDefaultSharedPreferences(requireContext())
        .getInt(getString(R.string.font_key), 2)

    readAdapter = ReadAdapter(fontSize + 14).also { binding.adapter = it }

    viewModel.getLyricsById(args.lyric).observe(viewLifecycleOwner) {
      binding.lyric = it[0]
      readAdapter.submitList(it)
    }

    (requireActivity() as AppCompatActivity).apply {
      setSupportActionBar(binding.toolbar)
      with(supportActionBar!!) {
        setHomeButtonEnabled(true)
        setDisplayHomeAsUpEnabled(true)
        setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        title = lyric.title
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
      Utils.createDynamicLink(
        requireParentFragment(),
        args.lyric,
        viewModel.firebaseAnalytics
      )
    }

    viewModel.update(
      args.lyric.copy(
        topPickHit = args.lyric.topPickHit.plus(1),
        timestamp = System.currentTimeMillis()
      )
    )
    binding.recyclerView.itemAnimator = null
    binding.recyclerView.setHasFixedSize(true)

    viewModel.apply {
      firebaseAnalytics.logEvent(Tag.TITLE, bundleOf(Pair(Tag.TITLE, args.lyric.title)))
      firebaseAnalytics.logEvent(Tag.NUMBER, bundleOf(Pair(Tag.NUMBER, args.lyric.number)))
      Tag.screenView(firebaseAnalytics, Tag.READ)
    }
  }
}
