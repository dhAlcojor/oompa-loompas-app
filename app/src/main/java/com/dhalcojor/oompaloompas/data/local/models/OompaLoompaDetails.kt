package com.dhalcojor.oompaloompas.data.local.models

import com.squareup.moshi.Json

data class OompaLoompaDetails(
    val firstName: String,
    val lastName: String,
    val image: String,
    val profession: String,
    val age: Int,
    val gender: String,
    val description: String,
    val quota: String,
    val height: Int,
    val country: String,
    val email: String,
    val favorite: OompaLoompaFavorite,
)

data class OompaLoompaFavorite(
    val color: String,
    val food: String,
    @Json(name = "random_string") val randomString: String,
    val song: String,
)