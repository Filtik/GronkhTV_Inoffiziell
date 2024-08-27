package de.filtik.gronkhtv.objects

data class MovieList(
    var start: ArrayList<Movie> = ArrayList(),
    var last_visited: ArrayList<Movie> = ArrayList()
)