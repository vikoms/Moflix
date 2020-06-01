package com.example.movieapplication.ui.favorite

import android.view.View
import com.example.movieapplication.entity.Favorite

interface FavoriteCallback {
    fun onItemClicked(data: Favorite, view: View)
}