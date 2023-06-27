package com.dhalcojor.oompaloompas.data.mappers

import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaResult
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