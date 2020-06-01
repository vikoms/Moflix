package com.example.movieapplication.entity


data class Movie(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null,
    var backdrop: String? = null,
    var releaseDate: String? = null,
    var rating: Double? = null,
    var runtime: String? = null
) {

    var genres: ArrayList<Genre>? = null
}