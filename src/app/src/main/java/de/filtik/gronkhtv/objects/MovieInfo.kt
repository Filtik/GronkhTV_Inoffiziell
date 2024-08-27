package de.filtik.gronkhtv.classes

import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class MovieInfo(
    var id: Long = 0,
    var title: String? = null,
    var description: String? = null,
    var episode: Long = 0,
    var created_at: String? = null,
    var preview_url: String? = null,
    var video_length: Long = 0,
    var views: Long = 0,
    var tags: ArrayList<String> = ArrayList(),
    var chapters: ArrayList<MovieInfoChapter> = ArrayList(),
    var chat_replay: String
)

data class MovieInfoChapter(
    var id: Long = 0,
    var title: String = "",
    var offset: Long = 0,
    var game: MovieInfoChaptersGame = MovieInfoChaptersGame()
)
data class MovieInfoChaptersGame(
    var id: Long = 0,
    var title: String = "",
    var twitch_details: MovieInfoChaptersGameTD = MovieInfoChaptersGameTD()
)
data class MovieInfoChaptersGameTD(
    var id: Long = 0,
    var title: String = "",
    var thumbnail_url: String = ""
)