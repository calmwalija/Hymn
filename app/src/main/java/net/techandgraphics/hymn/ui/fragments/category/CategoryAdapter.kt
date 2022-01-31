package net.techandgraphics.hymn.ui.fragments.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentCategoryItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils

class CategoryAdapter(
    private val click: (Lyric) -> Unit,
    private val favorite: (Lyric) -> Unit
) : ListAdapter<Lyric, CategoryAdapter.ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
            executePendingBindings()
        }

        init {
            binding.root.setOnClickListener { click.invoke(currentList[absoluteAdapterPosition]) }
            binding.buttonFav.setOnClickListener { favorite.invoke(currentList[absoluteAdapterPosition]) }

        }
    }
}