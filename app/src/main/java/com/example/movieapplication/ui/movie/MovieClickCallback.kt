package com.example.movieapplication.ui.movie

import com.example.movieapplication.entity.Movie

interface MovieClickCallback {

    fun onMovieClicked(data: Movie)

}