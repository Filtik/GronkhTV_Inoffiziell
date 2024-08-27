package de.filtik.gronkhtv.videoLeanback

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.fragment.app.FragmentActivity
import de.filtik.gronkhtv.chat.MyChatFragment
import de.filtik.gronkhtv.R

class PlaybackActivity : FragmentActivity()  {
    private var chatOpen: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("PlaybackActivity", "onCreate")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_video)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.player_container, PlaybackVideoFragment())
                .commit()

            supportFragmentManager.beginTransaction()
                .replace(R.id.chat_container, MyChatFragment())
                .addToBackStack(null)
                .commit()
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        toggleChat()
    }

    fun getPlaybackVideoFragment(): PlaybackVideoFragment? {
        return supportFragmentManager.findFragmentById(R.id.player_container) as? PlaybackVideoFragment
    }

    fun toggleChat() {
        Log.d("PlaybackActivity", "toggleChat")
        val chatContainer = findViewById<FrameLayout>(R.id.chat_container)
        val chatLayout = chatContainer.layoutParams as LinearLayout.LayoutParams

        if (chatOpen) {
            chatLayout.weight = 0f
            chatOpen = false
        }
        else {
            chatLayout.weight = 2f
            chatOpen = true
        }
    }

    companion object {
        const val MOVIE = "Movie"
    }
}