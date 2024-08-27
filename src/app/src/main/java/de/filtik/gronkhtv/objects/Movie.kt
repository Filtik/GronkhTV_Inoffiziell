package de.filtik.gronkhtv.objects

import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class Movie(
    var id: Long = 0,
    var title: String? = null,
    var description: String? = null,
    var episode: Long = 0,
    var created_at: String? = null,
    var preview_url: String? = null,
    var video_length: Long = 0,
    var views: Long = 0,
    var tags: ArrayList<String> = ArrayList(),
    var position: Long = 0
) : Serializable {

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", episode='" + episode + '\'' +
                ", preview_url='" + preview_url + '\'' +
                ", video_length='" + video_length + '\'' +
                ", position='" + position + '\'' +
                '}'
    }
}