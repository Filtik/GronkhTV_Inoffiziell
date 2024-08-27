package de.filtik.gronkhtv.videoLeanback

import android.annotation.SuppressLint
import android.content.Context
import android.media.session.PlaybackState
import android.os.Handler
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.leanback.media.PlaybackBaseControlGlue
import androidx.leanback.media.PlaybackControlGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.OnActionClickedListener
import androidx.leanback.widget.PlaybackControlsRow
import androidx.leanback.widget.PlaybackRowPresenter
import androidx.leanback.widget.PlaybackTransportRowPresenter
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowPresenter
import de.filtik.gronkhtv.R
import de.filtik.gronkhtv.helper.FileManager
import de.filtik.gronkhtv.objects.Movie
import java.util.Timer
import java.util.TimerTask


class BasicTransportControlGlue(
    context: Context,
    playerAdapter: BasicMediaPlayerAdapter,
) :
    PlaybackTransportControlGlue<BasicMediaPlayerAdapter>(context, playerAdapter) {

    private val uiHandler = Handler(context.mainLooper)

    var movie: Movie = Movie()
    val mediaSession = MediaSessionCompat(context, "VideoPlayback")
    private val supportActionsNames = ArrayList<String>()
    private var playbackActivity: PlaybackActivity? = null
    private var playbackVideoFragment: PlaybackVideoFragment? = null

    // Primary actions
    private val fastForwardAction = PlaybackControlsRow.FastForwardAction(context)
    private val rewindAction = PlaybackControlsRow.RewindAction(context)
    private val centerAction = PlaybackControlsRow.PlayPauseAction(context)
    private val nextAction = PlaybackControlsRow.SkipNextAction(context)
    private val previousAction = PlaybackControlsRow.SkipPreviousAction(context)
    private val chatAction = ChatAction(context)

    private var bar_timer: Runnable? = null

    @SuppressLint("UseCompatLoadingForDrawables")
    inner class ChatAction(context: Context) : PlaybackControlsRow.MultiAction(R.layout.my_overlay_layout) {
        init {
            val drawables = arrayOf(
                context.getDrawable(R.drawable.baseline_view_sidebar_24),
            )
            setDrawables(drawables)
        }
    }

    fun setPlaybackActivity(activity: PlaybackActivity) {
        playbackActivity = activity
    }

    fun setPlaybackVideoFragment(fragment: PlaybackVideoFragment) {
        playbackVideoFragment = fragment
    }


    private var playbackState: Int = -1
        set(value) {
            if (field != value) {
                field = value
                invalidatePlaybackState() // We'll cover this function later on.
            }
        }

    init {
//        Log.i(TAG, "Creating new instance...")
        mediaSession.setCallback(SimpleMediaSessionCallback())
        playbackState = PlaybackState.STATE_NONE
    }

//    override fun onCreateRowPresenter(): PlaybackRowPresenter {
//        return CustomPlaybackControlsRowPresenter()
//    }

    override fun onCreatePrimaryActions(primaryActionsAdapter: ArrayObjectAdapter?) {
        Log.d("BasicTransConGlue", "onCreatePrimaryActions")

        primaryActionsAdapter ?: return
//        primaryActionsAdapter.add(previousAction)
//        primaryActionsAdapter.add(rewindAction)
//        super.onCreatePrimaryActions(primaryActionsAdapter)
//        primaryActionsAdapter.add(forwardAction)
//        primaryActionsAdapter.add(nextAction)
    }

    override fun onCreateSecondaryActions(secondaryActionsAdapter: ArrayObjectAdapter?) {
        secondaryActionsAdapter?.add(chatAction)
    }

    override fun onActionClicked(action: Action?) {
        Log.d(TAG, "onActionClicked $action")
        when (action) {
            fastForwardAction -> fastForward()
            rewindAction -> rewind()
            centerAction -> playPause()
            chatAction -> {
                playbackActivity?.toggleChat()
                playbackVideoFragment?.resizeVideo()
            }
            else -> super.onActionClicked(action)
        }
        onUpdateProgress()
    }

//    override fun onCreateRowPresenter(): PlaybackRowPresenter? {
//        val detailsPresenter: AbstractDetailsDescriptionPresenter =
//            object : AbstractDetailsDescriptionPresenter() {
//                override fun onBindDescription(viewHolder: ViewHolder, obj: Any) {
//                    val glue = obj as PlaybackBaseControlGlue<*>
//                    viewHolder.title.text = glue.title
//                    viewHolder.subtitle.text = glue.subtitle
//                }
//            }
//        val rowPresenter: PlaybackTransportRowPresenter = object : PlaybackTransportRowPresenter() {
////            override fun onBindRowViewHolder(vh: RowPresenter.ViewHolder, item: Any) {
////                Log.d("BasicTransConGlue", "onBindRowViewHolder")
////                super.onBindRowViewHolder(vh, item)
////                vh.onKeyListener = null
////            }
////
////            override fun onUnbindRowViewHolder(vh: RowPresenter.ViewHolder) {
////                Log.d("BasicTransConGlue", "onUnbindRowViewHolder")
////                super.onUnbindRowViewHolder(vh)
////                vh.onKeyListener = null
////            }
//
//            override fun setOnClickListener(
//                holder: Presenter.ViewHolder?,
//                listener: View.OnClickListener?
//            ) {
//                Log.d("BasicTransConGlue", "setOnClickListener")
//                return
//                super.setOnClickListener(holder, listener)
//            }
//
//            override fun setOnActionClickedListener(listener: OnActionClickedListener?) {
//                Log.d("BasicTransConGlue", "setOnActionClickedListener")
//                return
//                super.setOnActionClickedListener(listener)
//            }
//
//
//            @SuppressLint("MissingInflatedId", "ResourceType")
//            override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
//                Log.d("BasicTransConGlue", "createRowViewHolder")
//                val viewHolder = super.createRowViewHolder(parent)
//
//                val inflater = LayoutInflater.from(parent.context)
//                val view = inflater.inflate(R.layout.my_overlay_layout, parent, false)
//
//                val secondaryActionButton = view.findViewById<Button>(R.id.customSecondaryActionButton)
//
//                secondaryActionButton.setOnClickListener {
//                }
//
//                return ViewHolder(view)
//            }
//
////            override fun createRowViewHolder(parent: ViewGroup?): RowPresenter.ViewHolder {
////                Log.d("BasicTransConGlue", "createRowViewHolder")
////                val vh = super.createRowViewHolder(parent)
////                vh.onKeyListener = null
////                return vh
////            }
//        }
//        rowPresenter.setDescriptionPresenter(detailsPresenter)
//        return rowPresenter
//    }



//    /** Key code constant: Back key.  */
//    val KEYCODE_BACK = 4
//    /** Key code constant: Directional Pad Up key.
//     * May also be synthesized from trackball motions.  */
//    val KEYCODE_DPAD_UP = 19
//    /** Key code constant: Directional Pad Down key.
//     * May also be synthesized from trackball motions.  */
//    val KEYCODE_DPAD_DOWN = 20
//    /** Key code constant: Directional Pad Left key.
//     * May also be synthesized from trackball motions.  */
//    val KEYCODE_DPAD_LEFT = 21
//    /** Key code constant: Directional Pad Right key.
//     * May also be synthesized from trackball motions.  */
//    val KEYCODE_DPAD_RIGHT = 22
//    /** Key code constant: Directional Pad Center key.
//     * May also be synthesized from trackball motions.  */
//    val KEYCODE_DPAD_CENTER = 23
//    /** Key code constant: Menu key.  */
//    val KEYCODE_MENU = 82
//    /** Key code constant: Play/Pause media key.  */
//    val KEYCODE_MEDIA_PLAY_PAUSE = 85
//    /** Key code constant: Stop media key.  */
//    val KEYCODE_MEDIA_STOP = 86
//    /** Key code constant: Play Next media key.  */
//    val KEYCODE_MEDIA_NEXT = 87
//    /** Key code constant: Play Previous media key.  */
//    val KEYCODE_MEDIA_PREVIOUS = 88
//    /** Key code constant: Rewind media key.  */
//    val KEYCODE_MEDIA_REWIND = 89
//    /** Key code constant: Fast Forward media key.  */
//    val KEYCODE_MEDIA_FAST_FORWARD = 90

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
        Log.i(TAG, "onKey "+event.action.toString()+" keyCode "+keyCode+" event "+event)

        if (!host.isControlsOverlayVisible)
            return false

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.action == KeyEvent.ACTION_DOWN)
        {
            return false
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.action == KeyEvent.ACTION_DOWN)
        {
//            if (event.action != KeyEvent.ACTION_DOWN)
//                movie.position = currentPosition

            return false
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN)
        {
//            if (event.action != KeyEvent.ACTION_DOWN)
//                movie.position = currentPosition

            return false
        }

        if (isPlaying) {
            bar_timer?.let {
                uiHandler.removeCallbacks(it)
                bar_timer = null
            }

            bar_timer = Runnable {
                if (host.isControlsOverlayVisible) {
                    host.hideControlsOverlay(true)
                }
            }
            uiHandler.postDelayed(bar_timer!!, 5000)
        }

        return super.onKey(v, keyCode, event)
    }

    private inner class SimpleMediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            Log.d("SMSC", "MediaSession onPlay")
            this@BasicTransportControlGlue.play()
        }
        override fun onPause() {
            Log.d("SMSC", "MediaSession onPause")
            this@BasicTransportControlGlue.pause()
        }
//        override fun onSkipToNext() = this@BasicTransportControlGlue.next()
//        override fun onSkipToPrevious() = this@BasicTransportControlGlue.previous()
        override fun onRewind() {
            Log.d("SMSC", "MediaSession onRewind")
            this@BasicTransportControlGlue.rewind()
        }
        override fun onFastForward() {
            Log.d("SMSC", "MediaSession onFastForward")
            this@BasicTransportControlGlue.fastForward()
        }
        override fun onSeekTo(pos: Long) {
            Log.d("SMSC", "MediaSession onSeekTo")
            this@BasicTransportControlGlue.seekTo(pos)
        }
    }

    override fun onHostStart() {
        Log.d(TAG, "onHostStart")
        super.onHostStart()
        mediaSession.isActive = true
    }

    override fun onHostPause() {
        Log.d(TAG, "onHostPause")
        FileManager(context).updateMoviePosition(movie, currentPosition)
        super.onHostPause()
        mediaSession.isActive = false
    }

    override fun onDetachedFromHost() {
        Log.d(TAG, "onDetachedFromHost")
        FileManager(context).updateMoviePosition(movie, currentPosition)
        super.onDetachedFromHost()
        mediaSession.release()
    }

    override fun onUpdateBufferedProgress() {
//        Log.i(TAG, "onUpdateBufferedProgress()")
        super.onUpdateBufferedProgress()
        invalidatePlaybackState()
    }

    override fun onPreparedStateChanged() {
        Log.i(TAG, "onPreparedStateChanged()")
        super.onPreparedStateChanged()
        invalidatePlaybackState()
    }

    override fun onPlayCompleted() {
        Log.d(TAG, "onPlayCompleted")
        FileManager(context).deleteMovieCache(movie)
        super.onPlayCompleted()
        playbackState = PlaybackState.STATE_NONE
    }

    override fun play() {
        Log.d(TAG, "play")
        Log.d(TAG, "movie.position "+movie.position)

        super.play()
    }

    fun resume() {
        Log.d(TAG, "resume")
        super.play()
    }

    private fun playPause() {
        Log.d(TAG, "playPause")
        if (isPlaying) {
            FileManager(context).updateMoviePosition(movie, currentPosition)
            pause()
        }
        else
            play()
    }

    override fun pause() {
        Log.d(TAG, "pause")
        Log.d(TAG, "movie.position "+movie.position)
        FileManager(context).updateMoviePosition(movie, currentPosition)
        super.pause()
    }

//    override fun previous() {
//        Log.d(TAG, "previous")
//        super.previous()
//    }
//
//    override fun next() {
//        Log.d(TAG, "next")
//        super.next()
//    }


    fun onStartBuffering() {
        Log.d("BTCG_MediaSession", "onStartBuffering")
        playbackState = PlaybackState.STATE_BUFFERING
    }

    fun onFinishedBuffering() {
        Log.d("BTCG_MediaSession", "onFinishedBuffering")
        playbackState = when (isPlaying) {
            true -> PlaybackState.STATE_PLAYING
            else -> PlaybackState.STATE_PAUSED
        }
    }

    fun rewind() {
        Log.d(TAG, "rewind")
        playbackState = PlaybackState.STATE_REWINDING
        seekTo(currentPosition - 10_000)
    }

    fun fastForward() {
        Log.d(TAG, "fastForward")
        playbackState = PlaybackState.STATE_FAST_FORWARDING
        seekTo(currentPosition + 10_000)
    }

    private fun mediaSessionSupportedActions(): Long {
        val supportedActions = supportedActions
        val supportedActionsNames = ArrayList<String>()
        var mediaSessionSupportedActions = 0L
        if ((supportedActions and PlaybackControlGlue.ACTION_PLAY_PAUSE.toLong()) != 0L) {
            mediaSessionSupportedActions = PlaybackStateCompat.ACTION_PAUSE xor
                    PlaybackStateCompat.ACTION_PLAY xor
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
            supportedActionsNames.add("Play/Pause")
        }
        if ((supportedActions and PlaybackControlGlue.ACTION_REWIND.toLong()) != 0L) {
            mediaSessionSupportedActions = mediaSessionSupportedActions xor PlaybackStateCompat.ACTION_REWIND
            supportedActionsNames.add("Rewind")
        }
        if ((supportedActions and PlaybackControlGlue.ACTION_FAST_FORWARD.toLong()) != 0L) {
            mediaSessionSupportedActions = mediaSessionSupportedActions xor PlaybackStateCompat.ACTION_FAST_FORWARD
            supportedActionsNames.add("FastForward")
        }
        if ((supportedActions and PlaybackControlGlue.ACTION_SKIP_TO_NEXT.toLong()) != 0L) {
            mediaSessionSupportedActions = mediaSessionSupportedActions xor PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            supportedActionsNames.add("SkipNext")
        }
        if ((supportedActions and PlaybackControlGlue.ACTION_SKIP_TO_PREVIOUS.toLong()) != 0L) {
            mediaSessionSupportedActions = mediaSessionSupportedActions xor PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            supportedActionsNames.add("SkipPrevious")
        }
        val newSupportedActionsNames = supportedActionsNames.joinToString()
//        if (supportActionsNames != newSupportedActionsNames) {
//            supportActionsNames = newSupportedActionsNames
//            Log.i(TAG, "mediaSessionSupportedActions: $newSupportedActionsNames")
//        }
        return mediaSessionSupportedActions
    }

    private fun invalidatePlaybackState() {
//        Log.d(TAG, "invalidatePlaybackState")
        val playbackStateBuilder = PlaybackStateCompat.Builder()
            .setState(playbackState, currentPosition, 1.0F)
            .setActions(mediaSessionSupportedActions())
            .setBufferedPosition(bufferedPosition)
        mediaSession.setPlaybackState(playbackStateBuilder.build())
    }

    companion object {
        private val TAG = "BasicTransConGlue"
    }
}
