package com.dhalcojor.oompaloompas.data.local.models

data class OompaLoompaResult(
    val currentPage: Int,
    val totalPages: Int,
    val oompaLoompas: List<OompaLoompa>,
)
