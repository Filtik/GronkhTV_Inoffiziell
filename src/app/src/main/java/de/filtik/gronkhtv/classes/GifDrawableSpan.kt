package de.filtik.gronkhtv.classes

import android.graphics.drawable.Drawable
import android.text.style.DynamicDrawableSpan
import pl.droidsonroids.gif.GifDrawable

class GifDrawableSpan(private val gifByteArray: ByteArray) : DynamicDrawableSpan() {

    override fun getDrawable(): Drawable {
        return GifDrawable(gifByteArray)
    }
}
