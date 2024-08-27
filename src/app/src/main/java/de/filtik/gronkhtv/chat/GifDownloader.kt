package de.filtik.gronkhtv.chat

import android.graphics.drawable.Drawable
import android.os.AsyncTask
import java.io.InputStream
import java.net.URL

class GifDownloader : AsyncTask<String, Void, Drawable?>() {

    override fun doInBackground(vararg params: String): Drawable? {
        val imageUrl = params[0]
        var drawable: Drawable? = null
        try {
            val inputStream: InputStream = URL(imageUrl).openStream()
            val gifByteArray = inputStream.readBytes()
            drawable = ByteArrayGifDrawable(gifByteArray)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return drawable
    }

//    override fun onPostExecute(result: Drawable?) {
//        listener.onGifDownloaded(result)
//    }
//
//    interface OnGifDownloadedListener {
//        fun onGifDownloaded(drawable: Drawable?)
//    }
}
