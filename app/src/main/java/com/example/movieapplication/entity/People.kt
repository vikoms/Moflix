package com.example.movieapplication.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class People(
    var id: Int? = null,
    var name: String? = null,
    var departement: String? = null,
    var profilePath: String? = null,
    var gender: String? = null,
    var biography: String? = null,
    var birthday: String? = null,
    var placeOfBirth: String? = null,
    var charCredits: String? = null
) : Parcelable