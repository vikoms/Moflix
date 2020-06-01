package com.example.movieapplication.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Series(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null,
    var poster: String? = null,
    var backdrop: String? = null,
    var releaseDate: String? = null,
    var rating: Double? = null,
    var episodeTotal: Int? = null
) : Parcelable {

    var genres: ArrayList<Genre>? = null
    var seasons: ArrayList<Season>? = null

}