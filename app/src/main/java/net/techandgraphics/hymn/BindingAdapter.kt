package net.techandgraphics.hymn

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.google.android.material.appbar.SubtitleCollapsingToolbarLayout
import com.squareup.picasso.Picasso
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.utils.Constant


@BindingAdapter("setImageView")
fun setImageView(imageView: AppCompatImageView, drawableRes: Int) {
    Picasso.get().load(Constant.images[drawableRes].drawableRes).into(imageView)
}


@BindingAdapter("setTextHymnNumber")
fun setTextHymnNumber(textView: AppCompatTextView, lyric: Lyric) {
    "Number ${lyric.number}".also { textView.text = it }
}


@BindingAdapter("setTextHymnInfo")
fun setTextHymnInfo(view: SubtitleCollapsingToolbarLayout, lyric: Lyric) {
    view.title = lyric.title
    view.subtitle = "Number ${lyric.number}"
}


@BindingAdapter("setTextHymnCount")
fun setTextHymnCount(textView: AppCompatTextView, count: Int) {
    "$count hymns".also { textView.text = it }
}


@BindingAdapter("setTag")
fun setTag(textView: AppCompatTextView, content: String) {
    "#$content".also { textView.text = it }
}