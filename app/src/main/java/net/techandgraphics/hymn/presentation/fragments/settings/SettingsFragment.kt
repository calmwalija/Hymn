package net.techandgraphics.hymn.presentation.fragments.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils
import net.techandgraphics.hymn.presentation.BaseViewModel
import java.util.*


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by viewModels<BaseViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        val themeValue: Array<String> =
            requireActivity().resources.getStringArray(R.array.theme_values)

        val versionValue: Array<String> =
            requireActivity().resources.getStringArray(R.array.version_values)

        val versionEntries: Array<String> =
            requireActivity().resources.getStringArray(R.array.version_entries)

        if (requireActivity().intent.getBooleanExtra(Constant.RESTART, false)) {
            requireActivity().intent.data = null
            requireActivity().intent.replaceExtras(Bundle())
            (if (PreferenceManager.getDefaultSharedPreferences(requireContext())
                    .getString(getString(R.string.version_key), versionValue[0]) == versionValue[0]
            ) versionEntries[0] else versionEntries[1]).also {
                Utils.toast(requireContext(), "$it version has been applied.")
            }
        }


        findPreference<ListPreference>(getString(R.string.version_key))?.let {
            it.summary =
                if (it.value.toString() == versionValue[0]) versionEntries[0] else versionEntries[1]
            it.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    it.summary =
                        if (newValue == versionValue[0]) versionEntries[0] else versionEntries[1]
                    if (it.value.toString() != newValue) {
                        Utils.restartApp(requireActivity())
                        viewModel.firebaseAnalytics.logEvent(
                            Tag.VERSION,
                            bundleOf(Pair(Tag.VERSION, newValue))
                        )
                    }
                    it.value.toString() != newValue
                }
        }


        findPreference<ListPreference>(getString(R.string.theme_key))?.let {
            it.summary =
                if (it.value.toString() == themeValue[0]) "System default" else it.value.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            it.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                when {
                    newValue.toString() == themeValue[1] -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    newValue.toString() == themeValue[2] -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                    else -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    }
                }
                viewModel.firebaseAnalytics.logEvent(Tag.THEME, bundleOf(Pair(Tag.THEME, newValue)))
                requireActivity().recreate()
                true
            }
        }


        findPreference<Preference>(getString(R.string.about_key))?.setOnPreferenceClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToAboutFragment().also {
                findNavController().navigate(it)
            }
            true
        }


        findPreference<Preference>(getString(R.string.resources_key))?.setOnPreferenceClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToOtherFragment().also {
                findNavController().navigate(it)
            }
            true
        }

        findPreference<SeekBarPreference>(getString(R.string.font_key))?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, p1 ->
                val newValue = p1.toString().toInt()
                viewModel.firebaseAnalytics.logEvent(Tag.FONT, bundleOf(Pair(Tag.FONT, newValue)))
                true
            }


        findPreference<Preference>(getString(R.string.rate_key))?.setOnPreferenceClickListener {
            val url = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"
            Utils.openWebsite(requireActivity(), url)
            true
        }

        findPreference<Preference>(getString(R.string.feedback_key))?.setOnPreferenceClickListener {
            val url = "https://api.whatsapp.com/send?phone=+265993563408"
            startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)))
            true
        }

        findPreference<Preference>(getString(R.string.clear_favorite_key))?.setOnPreferenceClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Attention")
                .setMessage("Are you sure you want to delete your favorite hymn list ?")
                .setNegativeButton("no", null)
                .setPositiveButton("yes") { _, _ ->
                    viewModel.clearFavorite()
                    viewModel.firebaseAnalytics.logEvent(
                        Tag.CLEAR_FAV, bundleOf(Pair(Tag.CLEAR_FAV, Tag.CLEAR_FAV))
                    )
                    Utils.toast(requireContext(), "Favorite hymn list deleted.")
                }
                .show()
            true
        }

        findPreference<Preference>(getString(R.string.clear_data_key))?.setOnPreferenceClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Attention")
                .setMessage("Are you sure you want to delete all your data ?")
                .setNegativeButton("no", null)
                .setPositiveButton("yes") { _, _ ->
                    viewModel.firebaseAnalytics.logEvent(
                        Tag.CLEAR_DATA, bundleOf(Pair(Tag.CLEAR_DATA, Tag.CLEAR_DATA))
                    )
                    viewModel.clear()
                }
                .show()
            true
        }
        viewModel.viewModelScope.launch {
            viewModel.channelTask.collectLatest {
                when (it) {
                    BaseViewModel.Callback.OnComplete -> {
                        Utils.toast(requireContext(), "App data has been reset.")
                        Utils.restartApp(requireActivity(), false)
                    }
                }
            }
        }


    }

}