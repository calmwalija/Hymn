package net.techandgraphics.hymn

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
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


@BindingAdapter("setTextHymnCount")
fun setTextHymnCount(textView: AppCompatTextView, count: Int) {
    "$count hymns".also { textView.text = it }
}


@BindingAdapter("setTag")
fun setTag(textView: AppCompatTextView, content: String) {
    "#$content".also { textView.text = it }
}

@BindingAdapter("favorite")
fun favorite(view: AppCompatImageView, favorite: Boolean) {
    view.setImageResource(if (favorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite)
}
