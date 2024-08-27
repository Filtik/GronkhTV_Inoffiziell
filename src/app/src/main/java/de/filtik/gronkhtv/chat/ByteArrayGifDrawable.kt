package de.filtik.gronkhtv.chat

import android.graphics.Canvas
import android.graphics.Movie
import android.graphics.drawable.Drawable

class ByteArrayGifDrawable(private val gifByteArray: ByteArray) : Drawable() {

    private var movie: Movie? = null
    private var startTime: Long = 0

    init {
        movie = Movie.decodeByteArray(gifByteArray, 0, gifByteArray.size)
        startTime = System.currentTimeMillis()
    }

    override fun draw(canvas: Canvas) {
        val currentTime = System.currentTimeMillis()
        val duration = movie?.duration() ?: 0

        if (duration == 0) {
            return  // Um eine Division durch Null zu vermeiden
        }

        val elapsedTime = (currentTime - startTime) % duration
        movie?.setTime(elapsedTime.toInt())
        movie?.draw(canvas, 0f, 0f)
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        // Nicht unterstützt
    }

    override fun setColorFilter(colorFilter: android.graphics.ColorFilter?) {
        // Nicht unterstützt
    }

    override fun getOpacity(): Int {
        return android.graphics.PixelFormat.OPAQUE
    }
}
