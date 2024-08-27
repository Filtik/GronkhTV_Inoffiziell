package de.filtik.gronkhtv

import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import de.filtik.gronkhtv.classes.MovieInfoChapter
import java.time.Duration
import java.time.LocalTime
import kotlin.properties.Delegates

/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class ChaptersPresenter : Presenter() {
    private var mDefaultCardImage: Drawable? = null
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
//        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)
        mDefaultCardImage = ContextCompat.getDrawable(parent.context, R.drawable.movie)

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return Presenter.ViewHolder(cardView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val mic = item as MovieInfoChapter
        val cardView = viewHolder.view as ImageCardView

//        Log.d(TAG, "onBindViewHolder")
        cardView.titleText = mic.title
        cardView.contentText = String.format("%02d:%02d:%02d", mic.offset / 3600, (mic.offset % 3600) / 60, mic.offset % 60)

        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)
        Glide.with(viewHolder.view.context)
            .load(mic.game.twitch_details.thumbnail_url.replace("{width}", CARD_WIDTH.toString()).replace("{height}", CARD_HEIGHT.toString()))
            .centerCrop()
            .error(mDefaultCardImage)
            .into(cardView.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) sSelectedBackgroundColor else sDefaultBackgroundColor
        // Both background colors should be set because the view"s background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }

    companion object {
        private val TAG = "CardPresenter"

//        private val CARD_WIDTH = 313
//        private val CARD_HEIGHT = 176
        private val CARD_WIDTH = 200
        private val CARD_HEIGHT = 267
    }
}