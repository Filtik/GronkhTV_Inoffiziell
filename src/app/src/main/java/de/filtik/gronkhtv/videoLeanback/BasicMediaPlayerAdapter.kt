package de.filtik.gronkhtv.videoLeanback

import android.content.Context
import android.media.session.PlaybackState.*
import androidx.leanback.media.MediaPlayerAdapter

class BasicMediaPlayerAdapter(context: Context) : MediaPlayerAdapter(context) {

//    override fun fastForward() {
//        seekTo(currentPosition + 10_000)
//    }
//
//    override fun rewind() = seekTo(currentPosition - 10_000)

//    init {
//        setProgressUpdatingEnabled(false)
//    }



    override fun getSupportedActions(): Long {
//        return (ACTION_SKIP_TO_PREVIOUS xor
//                ACTION_REWIND xor
//                ACTION_PLAY_PAUSE xor
//                ACTION_FAST_FORWARD xor
//                ACTION_SKIP_TO_NEXT).toLong()
        return (ACTION_REWIND xor
                ACTION_PLAY_PAUSE xor
                ACTION_FAST_FORWARD).toLong()
    }
}