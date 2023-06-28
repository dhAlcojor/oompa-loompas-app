package com.dhalcojor.oompaloompas.data.remote.models

import com.squareup.moshi.Json

data class OompaLoompaDetailsResponse(
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    val gender: String,
    val image: String,
    val profession: String,
    val age: Int,
    val description: String,
    val quota: String,
    val height: Int,
    val country: String,
    val email: String,
    val favorite: OompaLoompaFavoriteResponse,
)

data class OompaLoompaFavoriteResponse(
    val color: String,
    val food: String,
    @Json(name = "random_string") val randomString: String,
    val song: String,
)