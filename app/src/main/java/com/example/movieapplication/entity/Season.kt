package com.example.movieapplication.entity

data class Season(
    var name: String? = null,
    var id: Int? = null,
    var seasonNumber: Int? = null,
    var episodeCount: Int? = null,
    var releaseDate: String? = null,
    var overview: String? = null,
    var poster: String? = null
)