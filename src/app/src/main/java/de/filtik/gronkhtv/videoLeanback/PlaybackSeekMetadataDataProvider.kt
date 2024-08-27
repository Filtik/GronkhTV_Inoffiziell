//package de.filtik.test2.video
//
//import android.content.Context
//import android.media.MediaMetadataRetriever
//import android.net.Uri
//import androidx.leanback.widget.PlaybackSeekDataProvider
//
//
//class PlaybackSeekMetadataDataProvider(
//    context: Context,
//    videoUrl: String,
//    interval: Long,
//) :
//    PlaybackSeekDataProvider() {
//
//    private var mContext = context
//    private var mVideoUrl = videoUrl
//    private var mSeekPositions: LongArray
//
//    init {
//        mContext = context
//        mVideoUrl = videoUrl
//        val retriever = MediaMetadataRetriever()
//        retriever.setDataSource(mContext, Uri.parse(mVideoUrl))
//        val duration =
//            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
//        val size = (duration / interval).toInt() + 1
//        mSeekPositions = LongArray(size)
//        for (i in 0 until size) {
//            mSeekPositions[i] = i * interval
//        }
//    }
//}