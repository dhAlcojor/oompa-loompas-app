package com.dhalcojor.oompaloompas.data.local.models

data class OompaLoompa(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val image: String,
    val profession: String,
    val age: Int,
    val gender: String,
) {
    operator fun get(filter: String): String {
        return when (filter) {
            gender -> gender
            profession -> profession
            else -> ""
        }
    }
}
