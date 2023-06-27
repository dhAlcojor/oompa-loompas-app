package com.dhalcojor.oompaloompas.data.remote.models

data class OompaLoompaResponse(
    val current: Int,
    val total: Int,
    val results: List<OompaLoompaResultResponse>
)

data class OompaLoompaResultResponse(
    val first_name: String,
    val last_name: String,
    val favorite: OompaLoompaFavoriteResponse,
    val gender: String,
    val image: String,
    val profession: String,
    val age: Int,
    val country: String,
    val height: Int,
    val id: Int,
)

data class OompaLoompaFavoriteResponse(
    val color: String,
    val food: String,
    val random_string: String,
    val song: String,
)
