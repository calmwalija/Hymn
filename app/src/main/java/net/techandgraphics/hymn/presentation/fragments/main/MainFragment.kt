package net.techandgraphics.hymn.presentation.fragments.main

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.Utils.dialog
import net.techandgraphics.hymn.Utils.dialogShow
import net.techandgraphics.hymn.databinding.FragmentMainBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.onBubbleShowCaseBuilder
import net.techandgraphics.hymn.onChangeBook
import net.techandgraphics.hymn.tagEvent

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

  private val viewModel by viewModels<MainViewModel>()
  private var changeBookGuard = 0
  private var changeBookJob: Job? = null
  private lateinit var sharedPrefs: SharedPreferences
  private lateinit var dialog: Dialog

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    with(FragmentMainBinding.bind(view)) {
      changeBookGuard = 0
      sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
      featureAdapter = FeatureAdapter {
        MainFragmentDirections
          .actionMainFragmentToCategoryFragment(it)
          .apply {
            findNavController().navigate(this)
          }
      }.apply {
        viewModel.featuredHymn.onEach {
          submitList(it)
        }.launchIn(lifecycleScope)
      }

      ofTheDayAdapter = OfTheDayAdapter {
        when (it) {
          is OfTheDayAdapter.Event.Click ->
            MainFragmentDirections
              .actionLyricFragmentToReadFragment(it.lyric)
              .apply {
                findNavController().navigate(this)
                viewModel.firebaseAnalytics.tagEvent(
                  Tag.HYMN_OF_THE_DAY,
                  bundleOf(Pair(Tag.HYMN_OF_THE_DAY, it.lyric.title))
                )
              }

          is OfTheDayAdapter.Event.Favorite -> {
            viewModel.update(it.lyric)
            toast(it.lyric)
            viewModel.firebaseAnalytics.tagEvent(Tag.HYMN_OF_THE_DAY_FAV, bundleOf())
          }
        }
      }.apply {
        viewModel.ofTheDay.onEach {
          submitList(it)
        }.launchIn(lifecycleScope)
      }

      theHymnBook = TheHymnBook {
        MainFragmentDirections
          .actionLyricFragmentToReadFragment(it)
          .apply {
            findNavController().navigate(this)
          }
        viewModel.firebaseAnalytics.tagEvent(Tag.HYMN_BOOK, bundleOf(Pair(Tag.HYMN_BOOK, it.title)))
      }.apply {
        viewModel.theHymn.onEach {
          submitList(it)
        }.launchIn(lifecycleScope)
      }

      val versionValue = requireActivity().resources.getStringArray(R.array.version_values)
      bookSwitch.setOnCheckedChangeListener { _, isChecked ->
        if (changeBookGuard == 0) return@setOnCheckedChangeListener
        val newValue = if (isChecked.not()) versionValue.first() else versionValue.last()
        val oldValue = sharedPrefs.getString(getString(R.string.version_key), versionValue.first())
        if (oldValue == newValue) {
          changeBookJob?.cancel()
          return@setOnCheckedChangeListener
        }
        changeBookJob = viewModel.viewModelScope.launch {
          changeBookJob?.cancel()
          delay(700)
          sharedPrefs.edit().putString(getString(R.string.version_key), newValue).apply()
          findNavController() onChangeBook newValue.toString()
          viewModel.firebaseAnalytics.tagEvent(
            Tag.BOOK_SWITCH,
            bundleOf(Pair(Tag.BOOK_SWITCH, newValue))
          )
        }
      }

      toSearch.setOnClickListener {
        MainFragmentDirections
          .actionMainFragmentToSearchFragment()
          .apply {
            findNavController().navigate(this)
            viewModel.firebaseAnalytics.tagEvent(Tag.SEARCH_VIEW, bundleOf())
          }
      }

      toDiscover.setOnClickListener {
        MainFragmentDirections
          .actionMainFragmentToDiscoverFragment()
          .apply {
            findNavController().navigate(this)
            viewModel.firebaseAnalytics.tagEvent(Tag.DISCOVER_VIEW, bundleOf())
          }
      }
      setBook(bookSwitch, versionValue)

      viewModel.onBoarding.onEach {
        if (it.not()) {
          onBubbleShowCaseSequence(bookSwitch, toSearch, toDiscover)
          viewModel.onBoarding()
        }
      }.launchIn(lifecycle.coroutineScope)
    }
  }

  private fun Activity.langBubbleShowCaseBuilder(view: View) =
    onBubbleShowCaseBuilder(
      view,
      "Switch Book",
      "Tap here to switch between English and Chichewa hymn books"
    )

  private fun Activity.searchBubbleShowCaseBuilder(view: View) =
    onBubbleShowCaseBuilder(
      view,
      "Search Hymn",
      "Tap here to search for a hymn by title or number"
    )

  private fun Activity.discoverBubbleShowCaseBuilder(view: View) =
    onBubbleShowCaseBuilder(
      view,
      "Discover Hymn",
      "Tap here to discover new hymns"
    )

  private fun onBubbleShowCaseSequence(
    switchLang: View,
    toSearch: View,
    toDiscover: View
  ) = with(requireActivity()) {
    BubbleShowCaseSequence()
      .addShowCase(langBubbleShowCaseBuilder(switchLang))
      .addShowCase(searchBubbleShowCaseBuilder(toSearch))
      .addShowCase(discoverBubbleShowCaseBuilder(toDiscover))
      .show()
  }

  @SuppressLint("UseSwitchCompatOrMaterialCode")
  private fun setBook(view: Switch, versionValue: Array<String>) =
    sharedPrefs.getString(getString(R.string.version_key), versionValue.first())?.let {
      view.isChecked = it == versionValue.last()
      changeBookGuard++
    }

  private fun toast(lyric: Lyric) =
    requireContext().apply {
      Utils.toast(
        this,
        if (lyric.favorite.not()) getString(R.string.add_favorite, lyric.number) else
          getString(R.string.remove_favorite, lyric.number)
      )
    }

  private fun donate() {
    dialog = Dialog(requireContext()).dialog()
    dialog.apply {
      setContentView(R.layout.dialog_donate)
      findViewById<View>(R.id.closeButton).setOnClickListener { dismiss() }
      dialogShow()
    }
  }
}
