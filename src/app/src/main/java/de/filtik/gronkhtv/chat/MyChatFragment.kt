package de.filtik.gronkhtv.chat

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.*
import android.text.style.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import de.filtik.gronkhtv.R
import de.filtik.gronkhtv.classes.MovieInfo
import de.filtik.gronkhtv.helper.ReadWebPage
import de.filtik.gronkhtv.objects.*
import de.filtik.gronkhtv.videoLeanback.PlaybackActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream
import java.lang.Math.round
import java.net.URL
import java.util.Timer
import java.util.TimerTask
import kotlin.math.roundToInt

class MyChatFragment: Fragment() {

//    private lateinit var videoSourceProvider: VideoSourceProvider
    private val mHandler = Handler(Looper.myLooper()!!)

    private lateinit var movie: Movie
    private lateinit var movieInfo: MovieInfo
    private lateinit var recyclerView: RecyclerView
    private var viewContainer: ViewGroup? = null
    private val messageList = mutableListOf<Spanned>()
    private lateinit var messageAdapter: MessageAdapter
    private var lastSendMessagePosition: Long = 0
    private var currentChunk: Int = 0
    private var currentTime: Long = 0
    private var timer: Timer? = null
    private val chatInfoList: MutableMap<Int, ChatInfo> = mutableMapOf<Int, ChatInfo>()
    private val imageList: MutableMap<String, Drawable> = mutableMapOf<String, Drawable>()
    private val imageListAnimated: MutableMap<String, ImageSpan> = mutableMapOf<String, ImageSpan>()
    private var badgeList: BadgeSets? = null
    private lateinit var betterEmotesList: ArrayList<BetterEmotes>
//    var emoteList: MutableMap<String, String> = mutableMapOf<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("MyChatFragment", "onCreate")
        super.onCreate(savedInstanceState)

        movie = activity?.intent?.getSerializableExtra(PlaybackActivity.MOVIE) as Movie
        movieInfo = ReadWebPage.videoInfo(movie.episode)

        badgeList = ReadWebPage.getChatBadges()
        betterEmotesList = ReadWebPage.getBetterEmotes()

        //println("badgeList")
        //println(badgeList)

        checkChunk()
        startBackgroundTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        viewContainer = container

        recyclerView = container?.findViewById(R.id.chat_recycler_view)!!
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        messageAdapter = MessageAdapter(messageList)
        recyclerView.adapter = messageAdapter


        recyclerView.apply {
            layoutManager = recyclerView.layoutManager
            adapter = recyclerView.adapter
            setHasFixedSize(true)
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun fillEmote() {
        try {

        }
        catch (ex: java.lang.Exception) {
            Log.e(TAG, ex.message.toString())
        }
    }

    fun fetchChat(chat_num: Int) {
        try {
            val coroutineScope = CoroutineScope(Dispatchers.Main)

            coroutineScope.launch {
                val ci = ReadWebPage.chatInfo(movieInfo.chat_replay, chat_num)
                chatInfoList[chat_num] = ci
            }
        }
        catch (ex: java.lang.Exception) {
            Log.e(TAG, ex.message.toString())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun formatText(message: ChatInfoMessage): Spanned {
        val color = message.color.takeIf { it.isNotEmpty()  } ?: "#FFFFFF"

        val spannableBadges = SpannableStringBuilder()
        val spannableDisplayName = SpannableString(message.display_name)
        var spannableMessage = SpannableString(message.message)
        /*
        // Badges
        try {
            if (message.badges.isNotEmpty()) {
                val badges = message.badges.split(",")
                var countAdd = 0
                for (badge: String in badges) {
                    val badgeInfo = badge.split("/")

                    spannableBadges.append("  ")

                    if (imageList.containsKey(badge)) {
                        val imageSpan = imageList[badge]?.let { it1 -> ImageSpan(it1, ImageSpan.ALIGN_BASELINE) }
                        spannableBadges.setSpan(imageSpan, 0+countAdd, 1+countAdd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        countAdd += 2
                        continue
                    }

                    val findBadge = badgeList?.data?.find { emote -> emote.set_id == badgeInfo[0] }
                    val url = findBadge?.versions?.find { badge -> badge.id == badgeInfo[1] }?.image_url_2x

                    val imageLoader = ImageLoader()
                    imageLoader.execute(url)

                    val drawable: Drawable? = imageLoader.get()
                    if (drawable != null) {
                        drawable.setBounds(0, 0, 24, 24)

                        imageList[badge] = drawable

                        // ImageSpan zum Einfügen des Bildes in den Text erstellen
                        val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                        spannableBadges.setSpan(imageSpan, 0+countAdd, 1+countAdd, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    }

                    countAdd += 2
                }
                spannableBadges.append(" ")
            }
        }
        catch (ex: Exception) {
            Log.e(TAG, ex.message.toString())
        }
        */

        //Chat Emotes
        try {
            if (message.emote_data.isNotEmpty()) {
                val splitDiffEmotes = message.emote_data.split("/")
                for (splitDiffEmoteCounts: String in splitDiffEmotes) {
                    val splitDiffEmote = splitDiffEmoteCounts.split(":")
                    val emotePoses = splitDiffEmote[1].split(",")
                    val emoteId = splitDiffEmote[0]

                    for (pos in emotePoses.reversed()) {
                        val posSplit = pos.split("-")

                        val url = "https://static-cdn.jtvnw.net/emoticons/v2/$emoteId/default/dark/1.0"

                        emoteList[spannableMessage.subSequence(posSplit[0].toInt(), posSplit[1].toInt()+1).toString()] = url

                        if (imageList.containsKey(emoteId)) {
                            val imageSpan = imageList[emoteId]?.let { it1 -> ImageSpan(it1, ImageSpan.ALIGN_BASELINE) }
//                            val imageView = imageList[emoteId]
//                            val replacedString = spannableMessage.toString().replace(Regex(emoteId), url)
//                            spannableMessage = SpannableString(replacedString)
                            spannableMessage.setSpan(imageSpan, posSplit[0].toInt(), posSplit[1].toInt()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                            continue
                        }

                        val imageLoader = ImageLoader()
                        imageLoader.execute(url)


//                        val replacedString = spannableMessage.toString().replaceRange(posSplit[0].toInt(), posSplit[1].toInt()+1, url)
//                        spannableMessage = SpannableString(replacedString)

//                        val imageLoader = GifDownloader()
//                        imageLoader.execute(url)

                        val drawable: Drawable? = imageLoader.get()
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 28, 28)
                            imageList[emoteId] = drawable

                            // ImageSpan zum Einfügen des Bildes in den Text erstellen
                            val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)

                            spannableMessage.setSpan(imageSpan, posSplit[0].toInt(), posSplit[1].toInt()+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        }
                    }
                }
            }
        }
        catch (ex : Exception) {
            Log.e(TAG, ex.message.toString())
        }


//        try {
//            spannableMessage = replaceBetterEmotes(spannableMessage)
//        }
//        catch (ex: Exception){}
//
//        println(spannableMessage)

        val splitMessage = spannableMessage.split(" ")
        val hasAction = splitMessage[0].contains("ACTION")
        if (hasAction) {
            spannableMessage = SpannableString(spannableMessage.removeRange(0, splitMessage[0].length))
        }

        val combinedSpannable = SpannableStringBuilder().apply {
            append(spannableBadges)
            append(spannableDisplayName)
            if (!hasAction) {
                append(": ")
            }
            append(spannableMessage)
        }

        val colorLength = if (hasAction) combinedSpannable.length else combinedSpannable.split(":")[0].length+1

        combinedSpannable.setSpan(ForegroundColorSpan(Color.parseColor(color)), 0, colorLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        combinedSpannable.setSpan(StyleSpan(Typeface.BOLD), 0, colorLength, Spannable.SPAN_INCLUSIVE_INCLUSIVE)

        return combinedSpannable
    }

    fun replaceBetterEmotes(message: SpannableString): SpannableString {
        val spannableMessage = SpannableString(message)
        val splitMessage = spannableMessage.split(" ")

        for (msg: String in splitMessage) {
            val founded = betterEmotesList.find { it.code == msg }
            if (founded != null) {
                val startIndex = spannableMessage.indexOf(msg)
                var lastIndex = startIndex

                while (lastIndex >= 0) {
                    if (imageList.containsKey(msg)) {
                        val imageSpan = imageList[msg]?.let { it1 -> ImageSpan(it1, ImageSpan.ALIGN_BASELINE) }
                        spannableMessage.setSpan(imageSpan, lastIndex, lastIndex + msg.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                    } else {
                        val url = "https://cdn.betterttv.net/emote/${founded.id}/1x"

                        val imageLoader = ImageLoader()
                        imageLoader.execute(url)

//                        val imageLoader = GifDownloader()
//                        imageLoader.execute(url)

                        val drawable: Drawable? = imageLoader.get()
                        if (drawable != null) {
                            drawable.setBounds(0, 0, 28, 28)
                            imageList[msg] = drawable

                            val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                            spannableMessage.setSpan(imageSpan, lastIndex, lastIndex + msg.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
                        }
                    }

                    lastIndex = spannableMessage.indexOf(msg, lastIndex + msg.length)
                }
            }
        }

        return spannableMessage
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.N)
    fun updateMessage(cim: ChatInfoMessage) {
        val msg = formatText(cim)

        if (messageList.size >= 100) {
            messageList.subList(messageList.size-50, messageList.size).clear()
            messageAdapter.notifyDataSetChanged()
        }

        messageList.add(0, msg)
        messageAdapter.notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    fun sendNextChatMessage() {
        val playbackVideoFragment = (activity as PlaybackActivity).getPlaybackVideoFragment()
        val currentPosition = playbackVideoFragment?.getCurrentPosition()
        if (currentPosition != null) {
            currentTime = currentPosition
        }

        if (lastSendMessagePosition == currentTime)
            return

        checkChunk()

        if (lastSendMessagePosition > currentTime) {
            messageList.clear()
            messageAdapter.notifyDataSetChanged()
            lastSendMessagePosition = currentTime
            return
        }

        if (chatInfoList.containsKey(currentChunk)) {
            for (messageInfo: ChatInfoMessage in chatInfoList[currentChunk]?.messages!!) {
                if (messageInfo.offset != currentTime)
                    continue

                updateMessage(messageInfo)
            }
        }

        lastSendMessagePosition = currentTime
    }

    private fun startBackgroundTimer() {
        timer?.cancel()
        timer = Timer()
        timer?.scheduleAtFixedRate(UpdateBackgroundTask(), 0, 1000)
    }

    private inner class UpdateBackgroundTask : TimerTask() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun run() {
            mHandler.post { sendNextChatMessage() }
        }
    }

    private fun checkChunk() {
        val chunk = (currentTime / 300).toDouble().roundToInt()
        currentChunk = chunk
        if (!chatInfoList.containsKey(chunk)) {
            fetchChat(chunk)
        }
    }

    companion object {
        val emoteList: MutableMap<String, String> = mutableMapOf()
        private val TAG = "MyChatFragment"
    }
}