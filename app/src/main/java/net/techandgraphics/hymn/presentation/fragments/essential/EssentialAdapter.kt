package net.techandgraphics.hymn.presentation.fragments.essential

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentEssentialItemBinding
import net.techandgraphics.hymn.domain.model.Essential
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class EssentialAdapter(
  val onClick: (Essential) -> Unit
) : ListAdapter<Essential, EssentialAdapter.ViewHolder>(DiffUtils.ESSENTIAL_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentEssentialItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentEssentialItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(essential: Essential) = binding.apply {
      this.essential = essential
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener {
        onClick.invoke(currentList[absoluteAdapterPosition])
      }
    }
  }
}
