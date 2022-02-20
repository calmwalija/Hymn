package net.techandgraphics.hymn.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import net.techandgraphics.hymn.models.Lyric


object Utils {
    fun getActionBar(context: Context) = (context as AppCompatActivity).supportActionBar


    fun readJsonFromAssetToString(context: Context, file: String): String? {
        return try {
            context.assets.open(file).bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            null
        }
    }

    fun (RecyclerView.Adapter<*>).stateRestorationPolicy() {
        stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    infix fun Context.share(lyricList: List<Lyric>) = with(Intent(Intent.ACTION_SEND)) {
        var index = 0
        buildString {
            append(lyricList[0].title.uppercase())
            append("\n\n")
            lyricList.forEach { lyric ->
                if (lyric.chorus == 0) {
                    index += 1
                    append(index)
                } else append("Chorus")
                append("\n")
                append(lyric.content)
                append("\n\n")
            }
        }.also { putExtra(Intent.EXTRA_TEXT, it) }
        type = "text/plain"
        startActivity(Intent.createChooser(this, "Share"))
    }


    fun decodeResource(context: Context, @DrawableRes drawableRes: Int): Bitmap =
        BitmapFactory.decodeResource(context.resources, drawableRes)

    fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()


    fun String.regexLowerCase() = replace(Regex("[_',.;!-\"?]"), "").lowercase()

    fun EditText.onAddTextChangedListener(onTextChangedCallback: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChangedCallback.invoke(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) = Unit
        })
    }

    fun toast(context: Context, message: String) =
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

    fun createDynamicLink(fragment: Fragment, lyric: Lyric, firebaseAnalytics: FirebaseAnalytics) {
        toast(fragment.requireContext(), "Processing, just a moment please ...")
        FirebaseDynamicLinks.getInstance().createDynamicLink()
            .setLink(Uri.parse(String.format("%s?id=%d", Constant.DEEP_LINK, lyric.lyricId)))
            .setDomainUriPrefix(Constant.DOMAIN_URI_PREFIX)
            .setAndroidParameters(DynamicLink.AndroidParameters.Builder().build())
            .setSocialMetaTagParameters(
                DynamicLink.SocialMetaTagParameters.Builder()
                    .setTitle(lyric.title)
                    .setDescription(lyric.content)
                    .setImageUrl(Uri.parse(Constant.LOGO_URL))
                    .build()
            )
            .buildShortDynamicLink()
            .addOnSuccessListener { result ->
                Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, result.shortLink.toString())
                    .setType("text/plain")
                    .also {
                        firebaseAnalytics.logEvent(
                            Tag.SHARE,
                            bundleOf(Pair(Tag.SHARE, lyric.number))
                        )
                        fragment.startActivity(
                            Intent.createChooser(it, "Share")
                        )
                    }
            }
            .addOnFailureListener {
                toast(
                    fragment.requireContext(),
                    "Could not process your request, please check your internet connection."
                )
            }
    }

    fun openWebsite(activity: Activity, url: String) = activity.startActivity(
        Intent(Intent.ACTION_VIEW)
            .setData(Uri.parse(url))
    )

}