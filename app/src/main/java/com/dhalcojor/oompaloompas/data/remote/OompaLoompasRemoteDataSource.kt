package com.dhalcojor.oompaloompas.data.remote

import com.dhalcojor.oompaloompas.data.local.di.IoDispatcher
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaDetailsResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResponse
import com.dhalcojor.oompaloompas.data.remote.services.OompaLoompasService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface OompaLoompasDataSource {
    suspend fun fetchOompaLoompas(page: Int): OompaLoompaResponse
    suspend fun fetchOompaLoompaDetails(id: Int): OompaLoompaDetailsResponse
}

class OompaLoompasRemoteDataSource @Inject constructor(
    private val oompaLoompasService: OompaLoompasService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : OompaLoompasDataSource {
    override suspend fun fetchOompaLoompas(page: Int): OompaLoompaResponse =
        withContext(ioDispatcher) {
            oompaLoompasService.fetchOompaLoompas(page)
        }

    override suspend fun fetchOompaLoompaDetails(id: Int): OompaLoompaDetailsResponse =
        withContext(ioDispatcher) {
            oompaLoompasService.fetchOompaLoompaDetails(id)
        }
}
