package com.dhalcojor.oompaloompas.data.mappers

import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResultResponse

fun OompaLoompaResultResponse.toOompaLoompa() = OompaLoompa(
    firstName = this.first_name,
    lastName = this.last_name,
    image = this.image,
    profession = this.profession,
    age = this.age,
    gender = this.gender,
)