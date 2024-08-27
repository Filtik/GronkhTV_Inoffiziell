package de.filtik.gronkhtv.helper

import android.content.Context
import android.util.Log
import androidx.leanback.media.PlaybackGlue
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.filtik.gronkhtv.objects.Movie
import java.io.File
import java.lang.Exception

class FileManager(context: Context) : PlaybackGlue(context) {

    fun getAllCacheMovies() : ArrayList<Movie> {
        val gson = Gson()

        try {
            val file = File(context?.filesDir, "movie_cache")
            if (file.exists()) {
                val json = file.inputStream().readBytes().toString(Charsets.UTF_8)

                val arrayListMovieType = object : TypeToken<ArrayList<Movie>>() {}.type
                val movies: ArrayList<Movie> =
                    gson.fromJson(json, arrayListMovieType)

                return movies
            }
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }

        return ArrayList()
    }

    fun getMoviePosition(movie : Movie) : Long {
        val gson = Gson()

        var file = File(context?.filesDir, "movie_cache")
        if (!file.exists()) {
            File(context?.filesDir, "movie_cache").createNewFile()
            file = File(context?.filesDir, "movie_cache")

            file.outputStream().use {
                it.write("[]".toByteArray())
            }

            return 0
        }

        try {
            val json = file.inputStream().readBytes().toString(Charsets.UTF_8)

            val arrayListMovieType = object : TypeToken<ArrayList<Movie>>() {}.type
            val movies: ArrayList<Movie> =
                gson.fromJson(json, arrayListMovieType)

            movies.distinctBy { it.id }

            val json_movie = movies.find { m: Movie -> m.id == movie.id }

            if (json_movie != null) {
                return json_movie.position
            }
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }

        return 0
    }

    fun updateMoviePosition(movie : Movie, currentPosition: Long) {
        val gson = Gson()

        var file = File(context?.filesDir, "movie_cache")
        if (!file.exists()) {
            File(context?.filesDir, "movie_cache").createNewFile()
            file = File(context?.filesDir, "movie_cache")

            file.outputStream().use {
                it.write("[]".toByteArray())
            }
        }

        try {
            val json = file.inputStream().readBytes().toString(Charsets.UTF_8)

            val arrayListMovieType = object : TypeToken<ArrayList<Movie>>() {}.type
            val movies: ArrayList<Movie> =
                gson.fromJson(json, arrayListMovieType)

            movies.distinctBy { it.id }

            val json_movie = movies.find { m: Movie -> m.id == movie.id }
            if (json_movie != null) {
                movies.remove(json_movie)
            }

            movie.position = currentPosition

            movies.add(0, movie)

            val json_res: String = gson.toJson(movies)

            file.outputStream().use {
                it.write(json_res.toByteArray())
            }
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    public fun deleteMovieCache(movie : Movie) {
        val gson = Gson()

        var file = File(context?.filesDir, "movie_cache")
        if (!file.exists()) {
            File(context?.filesDir, "movie_cache").createNewFile()
            file = File(context?.filesDir, "movie_cache")

            file.outputStream().use {
                it.write("[]".toByteArray())
            }
        }

        try {
            val json = file.inputStream().readBytes().toString(Charsets.UTF_8)

            val arrayListMovieType = object : TypeToken<ArrayList<Movie>>() {}.type
            val movies: ArrayList<Movie> =
                gson.fromJson(json, arrayListMovieType)

            val json_movie = movies.find { m: Movie -> m == movie }
            if (json_movie != null) {
                movies.remove(json_movie)
            }

            val json_res: String = gson.toJson(movies)

            file.outputStream().use {
                it.write(json_res.toByteArray())
            }
        }
        catch (e: Exception) {
            Log.e(TAG, e.message.toString())
        }
    }

    companion object {
        private val TAG = this.javaClass.name
    }
}