package net.techandgraphics.hymn.presentation.fragments.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Tag
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentFavoriteBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.BaseViewModel
import net.techandgraphics.hymn.presentation.adapters.TopPickAdapter
import net.techandgraphics.hymn.presentation.fragments.SwipeDecorator

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var topPick: TopPickAdapter
    private val viewModel: BaseViewModel by viewModels()
    private lateinit var bind: FragmentFavoriteBinding

    private fun removeFavorite(lyric: Lyric) {
        viewModel.update(lyric.copy(favorite = !lyric.favorite))
        Snackbar.make(
            requireView(),
            requireContext().getString(R.string.remove_favorite, lyric.number),
            Snackbar.LENGTH_SHORT
        )
            .setAction("undo") {
                viewModel.update(lyric.copy(favorite = true))
            }.show()
    }

    private fun onItemTouchHelper() =
        object : SwipeDecorator() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                removeFavorite(favoriteAdapter.currentList[viewHolder.absoluteAdapterPosition])
            }
        }.also { ItemTouchHelper(it).attachToRecyclerView(bind.recyclerViewAll) }

    private fun Lyric.navigateToReadFragment() =
        FavoriteFragmentDirections
            .actionFavoriteFragmentToReadFragment(this).apply {
                findNavController().navigate(this)
            }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bind = FragmentFavoriteBinding.bind(view)

        topPick = TopPickAdapter {
            FavoriteFragmentDirections
                .actionFavoriteFragmentToReadFragment(it).apply {
                    findNavController().navigate(this)
                }
        }.also { it.stateRestorationPolicy() }


        favoriteAdapter = FavoriteAdapter(click = {
            it.navigateToReadFragment()
        }, favorite = { removeFavorite(it) }).also { it.stateRestorationPolicy() }


        bind.topPickAdapter = topPick
        bind.favoriteAdapter = favoriteAdapter
        bind.recyclerViewRecent.itemAnimator = null

        viewModel.observeTopPickCategories().observe(viewLifecycleOwner) {
            topPick.submitList(it)
            bind.recent.isVisible = it.isEmpty().not() && it.size > 3
        }

        viewModel.observeFavoriteLyrics().observe(viewLifecycleOwner) {
            bind.noFav.isVisible = it.isEmpty()
            bind.fav.isVisible = it.isEmpty().not()
            favoriteAdapter.submitList(it)
        }

        onItemTouchHelper()
        Tag.screenView(viewModel.firebaseAnalytics, Tag.FAVORITE)

    }

}