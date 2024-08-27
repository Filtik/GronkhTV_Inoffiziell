package de.filtik.gronkhtv

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat

class MyMediaBrowserService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        // Erstellen der Media Session und des MediaPlayers
        mediaSession = MediaSessionCompat(this, "MyMediaSession")
        mediaPlayer = MediaPlayer()

        // Setzen der Callbacks für den MediaPlayer
        mediaPlayer.setOnPreparedListener { player ->
            mediaSession.setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(
                        PlaybackStateCompat.STATE_PLAYING,
                        player.currentPosition.toLong(),
                        1.0f
                    )
                    .build()
            )
            player.start()
        }

        mediaPlayer.setOnCompletionListener {
            mediaSession.setPlaybackState(
                PlaybackStateCompat.Builder()
                    .setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        mediaPlayer.currentPosition.toLong(),
                        1.0f
                    )
                    .build()
            )
        }

        // Setzen des MediaSessionCallbacks
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                mediaPlayer.prepareAsync()
            }

            override fun onPause() {
                mediaPlayer.pause()
            }

            override fun onStop() {
                mediaPlayer.stop()
                mediaSession.setPlaybackState(
                    PlaybackStateCompat.Builder()
                        .setState(
                            PlaybackStateCompat.STATE_STOPPED,
                            mediaPlayer.currentPosition.toLong(),
                            1.0f
                        )
                        .build()
                )
            }

            override fun onSeekTo(pos: Long) {
                mediaPlayer.seekTo(pos.toInt())
                mediaSession.setPlaybackState(
                    PlaybackStateCompat.Builder()
                        .setState(
                            PlaybackStateCompat.STATE_PLAYING,
                            pos,
                            1.0f
                        )
                        .build()
                )
            }
        })

        // Setzen der Media Session Metadata
        val mediaMetadata = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "My Audio Title")
            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "My Artist")
            .build()
        mediaSession.setMetadata(mediaMetadata)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?,
    ): BrowserRoot? {
        TODO("Not yet implemented")
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>,
    ) {
        TODO("Not yet implemented")
    }
}