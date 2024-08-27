package de.filtik.gronkhtv.videoLeanback

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.widget.*
import de.filtik.gronkhtv.ChaptersPresenter
import de.filtik.gronkhtv.chat.MyChatFragment
import de.filtik.gronkhtv.R
import de.filtik.gronkhtv.classes.*
import de.filtik.gronkhtv.helper.FileManager
import de.filtik.gronkhtv.helper.ReadWebPage
import de.filtik.gronkhtv.objects.Movie


/** Handles video playback with media controls. */
class PlaybackVideoFragment : VideoSupportFragment() {

    private val TAG = "PlaybackVideoFragment"
    private lateinit var transportControlGlue: BasicTransportControlGlue
    private var mChannelId: Long = 0
    private var mStartingPosition: Long = 0
    private lateinit var movie: Movie

    private var chapterRowAdapter: ArrayObjectAdapter = ArrayObjectAdapter(ChaptersPresenter())

    private lateinit var fastForwardIndicatorView: View
    private lateinit var rewindIndicatorView: View
    private lateinit var playIndicatorView: View
    private lateinit var pauseIndicatorView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState) as ViewGroup

        fastForwardIndicatorView = inflater.inflate(R.layout.view_playback_forward, view, false)
        view.addView(fastForwardIndicatorView)

        rewindIndicatorView = inflater.inflate(R.layout.view_playback_rewind, view, false)
        view.addView(rewindIndicatorView)

        playIndicatorView = inflater.inflate(R.layout.view_playback_play, view, false)
        view.addView(playIndicatorView)

        pauseIndicatorView = inflater.inflate(R.layout.view_playback_pause, view, false)
        view.addView(pauseIndicatorView)

//        // Get the playback overlay view
//        val playbackOverlay = container?.findViewById<ViewGroup>(R.id.player_container)
//
//        // Create a new button
//        val button = Button(requireContext())
//        button.text = "My Button"
//        button.setOnClickListener {
//            Log.d("PlaybackVideoFragment", "button")
//        }
//
//        // Add the button to the overlay view
//        playbackOverlay?.addView(button)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.BLACK)
        if (savedInstanceState == null) {
            (adapter.presenterSelector as ClassPresenterSelector)
                .addClassPresenter(ListRow::class.java, ListRowPresenter())
            chapterRowAdapter = ArrayObjectAdapter(ChaptersPresenter())


            val vi = ReadWebPage.videoInfo(movie.episode)
            chapterRowAdapter.setItems(vi.chapters, null)

            val chapterRow = ListRow(1L, HeaderItem("Kapitel"), chapterRowAdapter)
            (adapter as ArrayObjectAdapter).add(chapterRow)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("PlaybackVideoFragment", "onCreate")

        movie = activity?.intent?.getSerializableExtra(PlaybackActivity.MOVIE) as Movie
        movie.position = FileManager(requireContext()).getMoviePosition(movie)

        mChannelId = movie.id
        mStartingPosition = movie.position

        try {
            transportControlGlue = BasicTransportControlGlue(
                context = requireContext(),
                playerAdapter = BasicMediaPlayerAdapter(requireContext())
            )

            transportControlGlue.host = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
            transportControlGlue.isSeekEnabled = true
            transportControlGlue.movie = movie
            transportControlGlue.title = movie.title
            transportControlGlue.subtitle = movie.description

            val episode_url: String = ReadWebPage.episodeURL(movie.episode)
            println(episode_url)
            transportControlGlue.playerAdapter.setDataSource(Uri.parse(episode_url))

            val playbackActivity = activity as PlaybackActivity
//            playbackActivity?.setPlaybackActivity(playbackActivity)
            transportControlGlue.setPlaybackActivity(playbackActivity)
            transportControlGlue.setPlaybackVideoFragment(this)
        }
        catch (ex: java.lang.Exception) {
            Log.e(TAG, ex.message.toString())
        }

        transportControlGlue.controlsRow = transportControlGlue.controlsRow

//        PlaybackSeekDiskDataProvider.setDemoSeekProvider(transportControlGlue)

        setOnItemViewClickedListener { _, item, _, row ->
//            Log.d(TAG, "item "+item+" row "+row+" movie "+movie)
            if (row is ListRow && row.adapter == chapterRowAdapter) {
                val mic = item as MovieInfoChapter

                mStartingPosition = mic.offset * 1000
                seekToStartingPosition()
            }
        }

//        setOnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
//            if (!isControlsOverlayVisible)
//                return@setOnItemViewSelectedListener
//
//            setOnKeyInterceptListener { view, keyCode, event ->
//                if (isControlsOverlayVisible && keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN && rowViewHolder.row.id < 0) {
////                    Log.d(TAG, "Key UP - isControlsOverlayVisible $isControlsOverlayVisible")
//                    preventControlsOverlay(transportControlGlue)
//                }
//                false
//            }
//        }

//            Log.d(TAG, "setOnItemViewSelectedListener -- item "+item+" rowViewHolder "+rowViewHolder.row.id.toString()+" row "+row.toString())


            // Adds key listeners
            setOnKeyInterceptListener { view, keyCode, event ->
//                Log.i(TAG, "onKey event $event")

                // Early exit: if the controls overlay is visible, don't intercept any keys
//                Log.d(TAG, "isControlsOverlayVisible "+isControlsOverlayVisible)
                if (isControlsOverlayVisible) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN) {
//                        Log.d(TAG, "Key KEYCODE_DPAD_UP - isControlsOverlayVisible $isControlsOverlayVisible")
//                        preventControlsOverlay(transportControlGlue)
                        return@setOnKeyInterceptListener false
                    }

                    return@setOnKeyInterceptListener false
                }

                if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                    && event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Intercepting KEYCODE_DPAD_CENTER key for fragment navigation")
                    preventControlsOverlay(transportControlGlue)

                    if (transportControlGlue.isPlaying) {
                        transportControlGlue.pause()
                        animateIndicator(pauseIndicatorView)
                    }
                    else {
                        transportControlGlue.play()
                        animateIndicator(playIndicatorView)
                    }

                    return@setOnKeyInterceptListener false
                }

                if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Intercepting KEYCODE_DPAD_UP key for fragment navigation")
                    preventControlsOverlay(transportControlGlue)
                    return@setOnKeyInterceptListener false
                }

                // Rewinds when user presses DPAD_LEFT
                if (!isControlsOverlayVisible &&
                    keyCode == KeyEvent.KEYCODE_DPAD_LEFT && event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Intercepting KEYCODE_DPAD_LEFT key for fragment navigation")
                    transportControlGlue.rewind()
                    preventControlsOverlay(transportControlGlue)
                    animateIndicator(rewindIndicatorView)
                    return@setOnKeyInterceptListener false
                }

                // Skips ahead when user presses DPAD_RIGHT
                if (!isControlsOverlayVisible &&
                    keyCode == KeyEvent.KEYCODE_DPAD_RIGHT && event.action == KeyEvent.ACTION_DOWN) {
                    Log.d(TAG, "Intercepting KEYCODE_DPAD_RIGHT key for fragment navigation")
                    transportControlGlue.fastForward()
                    preventControlsOverlay(transportControlGlue)
                    animateIndicator(fastForwardIndicatorView)
                    return@setOnKeyInterceptListener false
                }

//                Log.d(TAG, "isControlsOverlayVisible2 "+isControlsOverlayVisible)

                false
            }

        seekToStartingPosition();
        playWhenReady(transportControlGlue)
    }

    private fun preventControlsOverlay(playerGlue: BasicTransportControlGlue) = view?.postDelayed({
        playerGlue.host.showControlsOverlay(false)
        playerGlue.host.hideControlsOverlay(false)
    }, 10)


    fun playWhenReady(glue: BasicTransportControlGlue) {
        if (glue.isPrepared) {
            glue.play()
        } else {
            glue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
                override fun onPreparedStateChanged(glue: PlaybackGlue) {
                    if (glue.isPrepared) {
                        glue.removePlayerCallback(this)
                        glue.play()
                    }
                }
            })
        }
    }

    private fun seekToStartingPosition() {
        // Skip ahead if given a starting position.
        if (mStartingPosition > -1L) {
            if (transportControlGlue.isPrepared) {
                Log.d("VideoFragment", "Is prepped, seeking to $mStartingPosition")
                transportControlGlue.seekTo(mStartingPosition)
            } else {
                transportControlGlue.addPlayerCallback(
                    object : PlaybackGlue.PlayerCallback() {
                        override fun onPreparedStateChanged(glue: PlaybackGlue) {
                            super.onPreparedStateChanged(glue)
                            if (transportControlGlue.isPrepared) {
                                transportControlGlue.removePlayerCallback(this)
                                Log.d(
                                    TAG,
                                    "In callback, seeking to $mStartingPosition"
                                )
                                transportControlGlue.seekTo(mStartingPosition)
                            }
                        }
                    })
            }
        }
    }

    private fun animateIndicator(indicatorView: View) {
        indicatorView.animate()
            .withEndAction {
                indicatorView.isVisible = false
                indicatorView.alpha = 1F
                indicatorView.scaleX = 1F
                indicatorView.scaleY = 1F
            }
            .withStartAction {
                indicatorView.isVisible = true
            }
            .alpha(0.2F)
            .scaleX(2f)
            .scaleY(2f)
            .setDuration(400)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    fun getCurrentPosition(): Long {
        return transportControlGlue.currentPosition / 1000
    }

    fun resizeVideo() {
        val videoSurfaceView = surfaceView
        val layoutParams = videoSurfaceView?.layoutParams as? FrameLayout.LayoutParams
        layoutParams?.width = LinearLayout.LayoutParams.MATCH_PARENT

        videoSurfaceView?.viewTreeObserver?.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                videoSurfaceView.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Breite des VideoSurfaceView abrufen
                val width = videoSurfaceView.width

                // Neue Höhe berechnen, um das Seitenverhältnis beizubehalten (z.B. 16:9 Seitenverhältnis)
                val newHeight = (width * 9) / 16

                val layoutParams = videoSurfaceView.layoutParams
                layoutParams.height = newHeight
                videoSurfaceView.layoutParams = layoutParams
            }
        })
    }
}