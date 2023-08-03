package net.techandgraphics.hymn.presentation.fragments.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.Utils.stateRestorationPolicy
import net.techandgraphics.hymn.databinding.FragmentFavoriteBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.fragments.SwipeDecorator

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

  private lateinit var favoriteAdapter: FavoriteAdapter
  private lateinit var recentAdapter: RecentAdapter
  private val viewModel: FavoriteViewModel by viewModels()
  private lateinit var bind: FragmentFavoriteBinding

  private fun removeFavorite(lyric: Lyric) {
    viewModel.update(lyric)
    Toast.makeText(
      requireContext(),
      requireContext().getString(R.string.remove_favorite, lyric.number),
      Toast.LENGTH_SHORT
    ).show()
  }

  private fun onItemTouchHelper() =
    object : SwipeDecorator() {
      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        removeFavorite(favoriteAdapter.currentList[viewHolder.absoluteAdapterPosition])
      }
    }.also { ItemTouchHelper(it).attachToRecyclerView(bind.recyclerViewAll) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    bind = FragmentFavoriteBinding.bind(view)

    recentAdapter = RecentAdapter {
      FavoriteFragmentDirections
        .actionFavoriteFragmentToReadFragment(it).apply {
          findNavController().navigate(this)
        }
    }.also { it.stateRestorationPolicy() }

    favoriteAdapter = FavoriteAdapter {
      FavoriteFragmentDirections
        .actionFavoriteFragmentToReadFragment(it).apply {
          findNavController().navigate(this)
        }
    }.also { it.stateRestorationPolicy() }

    bind.favoriteAdapter = favoriteAdapter

    viewModel.favorite.onEach {
      bind.noFav.isVisible = it.isEmpty()
      bind.fav.isVisible = it.isEmpty().not()
      favoriteAdapter.submitList(it)
    }.launchIn(lifecycleScope)

    onItemTouchHelper()
    viewModel.firebaseAnalytics()
  }
}
