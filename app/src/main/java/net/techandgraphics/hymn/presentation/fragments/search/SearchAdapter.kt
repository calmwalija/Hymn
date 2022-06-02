package net.techandgraphics.hymn.presentation.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentFavoriteItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.HYMN_DIFF_UTIL
import net.techandgraphics.hymn.presentation.fragments.search.SearchAdapter.ViewHolder

class SearchAdapter(
    private val click: (Lyric) -> Unit,
    private val favorite: (Lyric) -> Unit
) : PagingDataAdapter<Lyric, ViewHolder>(HYMN_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentFavoriteItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentFavoriteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
            executePendingBindings()
        }


        init {
            binding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { click.invoke(it) }
            }
            binding.buttonFav.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { favorite.invoke(it) }
            }
        }
    }

}