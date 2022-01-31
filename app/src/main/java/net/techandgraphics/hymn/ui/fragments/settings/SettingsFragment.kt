package net.techandgraphics.hymn.ui.fragments.settings

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.fragments.BaseViewModel
import net.techandgraphics.hymn.utils.Utils


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val url = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"
    private val viewModel by viewModels<BaseViewModel>()


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<Preference>("about")?.setOnPreferenceClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToAboutFragment().also {
                findNavController().navigate(it)
            }
            true
        }


        findPreference<Preference>("resources")?.setOnPreferenceClickListener {
            SettingsFragmentDirections.actionSettingsFragmentToOtherFragment().also {
                findNavController().navigate(it)
            }
            true
        }


        findPreference<Preference>("rate")?.setOnPreferenceClickListener {
            Utils.openWebsite(requireActivity(), url)
            true
        }

        findPreference<Preference>("feedback")?.setOnPreferenceClickListener {
            Utils.openWebsite(requireActivity(), url)
            true
        }

        findPreference<Preference>("clear_favorite")?.setOnPreferenceClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete all your favorite hymn list ?")
                .setNegativeButton("no", null)
                .setPositiveButton("yes") { _, _ ->
                    viewModel.clearFavorite()
                    Utils.toast(requireContext(),"Favorite hymn list deleted.")
                }
                .show()
            true
        }


    }

}