package com.dhalcojor.oompaloompas.data.mappers

import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaDetails
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaFavorite
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaResult
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaDetailsResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaFavoriteResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResultResponse

fun OompaLoompaResponse.toOompaLoompaResult() = OompaLoompaResult(
    currentPage = this.current,
    totalPages = this.total,
    oompaLoompas = this.results.map { it.toOompaLoompa() }
)

fun OompaLoompaResultResponse.toOompaLoompa() = OompaLoompa(
    id = this.id,
    firstName = this.firstName,
    lastName = this.lastName,
    image = this.image,
    profession = this.profession,
    age = this.age,
    gender = this.gender,
)

fun OompaLoompaDetailsResponse.toOompaLoompaDetails() = OompaLoompaDetails(
    firstName = this.firstName,
    lastName = this.lastName,
    image = this.image,
    profession = this.profession,
    age = this.age,
    gender = this.gender,
    description = this.description,
    quota = this.quota,
    height = this.height,
    country = this.country,
    email = this.email,
    favorite = this.favorite.toOompaLoompaFavorite()
)

fun OompaLoompaFavoriteResponse.toOompaLoompaFavorite() = OompaLoompaFavorite(
    color = this.color,
    food = this.food,
    randomString = this.randomString,
    song = this.song,
)