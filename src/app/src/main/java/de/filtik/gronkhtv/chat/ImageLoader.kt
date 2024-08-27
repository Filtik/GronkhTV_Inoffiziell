package de.filtik.gronkhtv.chat

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import java.io.InputStream
import java.net.URL

class ImageLoader : AsyncTask<String, Void, Drawable?>() {

    override fun doInBackground(vararg params: String): Drawable? {
        val imageUrl = params[0]
        var bitmap: Bitmap? = null
        var drawable: Drawable? = null
        try {
            val inputStream: InputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
            drawable = BitmapDrawable(Resources.getSystem(), bitmap)
        } catch (e: Exception) {
//            e.printStackTrace()
        }
        return drawable
    }
}
