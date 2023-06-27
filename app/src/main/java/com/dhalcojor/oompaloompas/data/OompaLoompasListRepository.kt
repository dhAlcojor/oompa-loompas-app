/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dhalcojor.oompaloompas.data

import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.data.mappers.toOompaLoompa
import com.dhalcojor.oompaloompas.data.remote.OompaLoompasRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface OompaLoompasListRepository {
    val oompaLoompas: List<OompaLoompa>
    suspend fun fetchOompaLoompas(page: Int?, refresh: Boolean = false): List<OompaLoompa>
}

class DefaultOompaLoompasListRepository @Inject constructor(
    private val oompaLoompasRemoteDataSource: OompaLoompasRemoteDataSource,
    private val externalScope: CoroutineScope,
) : OompaLoompasListRepository {

    // Mutex to make writes to cached values thread-safe.
    private val oompaLoompasMutex = Mutex()
    override var oompaLoompas: List<OompaLoompa> = emptyList()
    override suspend fun fetchOompaLoompas(page: Int?, refresh: Boolean): List<OompaLoompa> {
        if (refresh || oompaLoompas.isEmpty()) {
            withContext(externalScope.coroutineContext) {
                val networkResult = oompaLoompasRemoteDataSource.fetchOompaLoompas(page)
                oompaLoompasMutex.withLock {
                    oompaLoompas = networkResult.results.map { it.toOompaLoompa() }
                }
            }
        }

        return oompaLoompasMutex.withLock { oompaLoompas }
    }
}
