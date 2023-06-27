package com.dhalcojor.oompaloompas.data.remote.models

import com.squareup.moshi.Json

data class OompaLoompaResponse(
    val current: Int,
    val total: Int,
    val results: List<OompaLoompaResultResponse>
)

data class OompaLoompaResultResponse(
    val id: Int,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String,
    val gender: String,
    val image: String,
    val profession: String,
    val age: Int,
)
