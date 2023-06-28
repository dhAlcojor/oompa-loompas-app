package com.dhalcojor.oompaloompas.data.remote.services

import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaDetailsResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OompaLoompasService {
    @GET("oompa-loompas")
    suspend fun fetchOompaLoompas(@Query("page") page: Int): OompaLoompaResponse

    @GET("oompa-loompas/{id}")
    suspend fun fetchOompaLoompaDetails(@Path("id") id: Int): OompaLoompaDetailsResponse
}