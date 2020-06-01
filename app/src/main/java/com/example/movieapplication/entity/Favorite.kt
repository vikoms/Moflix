package com.example.movieapplication.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var favoriteId: Int? = 0,
    var title: String? = null,
    var backdrop: String? = null,
    var poster: String? = null,
    var rating: Double? = null,
    var type: String? = null,
    var releaseDate: String? = null
) : Parcelable