package net.techandgraphics.hymn.ui.fragments.other

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentOtherItemBinding
import net.techandgraphics.hymn.models.Other
import net.techandgraphics.hymn.ui.diffs.DiffUtils

class OtherAdapter(
    val fontSize: Int
) : ListAdapter<Other, OtherAdapter.ViewHolder>(DiffUtils.OTHER_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentOtherItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentOtherItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(other: Other) = binding.apply {
            this.other = other
            stanza.textSize = fontSize.toFloat()
            executePendingBindings()
        }

    }
}