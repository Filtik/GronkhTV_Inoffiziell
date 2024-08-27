package de.filtik.gronkhtv.classes

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import de.filtik.gronkhtv.helper.FileManager
import de.filtik.gronkhtv.helper.ReadWebPage
import de.filtik.gronkhtv.objects.Movie
import de.filtik.gronkhtv.objects.MovieList
import org.json.JSONObject
import java.lang.Exception


object MovieManager : Fragment() {
    val MOVIE_CATEGORY = arrayOf(
        "Start",
        "Zuletzt geschaut"
    )

    public fun setupMovies(context: Context): MovieList {

        val list: MovieList = MovieList()
        val movies: ArrayList<Movie> = ArrayList<Movie>()

        try {
            val json =
                ReadWebPage.fetch("https://api.gronkh.tv/v1/search?first=10&direction=desc&sort=date");

            for (i in 0 until json.length()) {
                val obj: JSONObject = json.getJSONObject(i)

                val movie = Movie()
                movie.id = obj.getLong("id")
                movie.title = obj.getLong("episode").toString()
                movie.description = obj.getString("title")
                movie.episode = obj.getLong("episode")
                movie.created_at = obj.getString("created_at")
                movie.preview_url = obj.getString("preview_url")
                movie.video_length = (obj.getLong("video_length") * 1000)
                movie.views = obj.getLong("views")
//            movie.tags = obj.getJSONArray("tags")

                movies.add(movie)
            }
        }
        catch (ex: Exception) {
            Log.e("MovieList", ex.message.toString())
        }

        list.start = movies
        list.last_visited = FileManager(context).getAllCacheMovies()

        return list
    }
}