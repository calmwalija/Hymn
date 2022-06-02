package net.techandgraphics.hymn.presentation.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerView(context: Context, attributeSet: AttributeSet?) :
    RecyclerView(context, attributeSet) {

    init {
        setHasFixedSize(true)
        layoutManager = GridLayoutManager(context, 2)
        isVerticalScrollBarEnabled = true
    }

}