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

import com.dhalcojor.oompaloompas.data.remote.OompaLoompasDataSource
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaDetailsResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaFavoriteResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResponse
import com.dhalcojor.oompaloompas.data.remote.models.OompaLoompaResultResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for [DefaultOompaLoompasListRepository].
 */
class DefaultOompaLoompasListRepositoryTest {

    @Test
    fun oompaLoompasLists_fetchOompaLoompas() = runTest {
        val repository = DefaultOompaLoompasListRepository(FakeOompaLoompasListDatasource(), this)
        val oompaLoompaResponse = repository.fetchOompaLoompas(1)
        val oompaLoompa = oompaLoompaResponse.oompaLoompas[0]
        assertEquals(oompaLoompaResponse.currentPage, 1)
        assertEquals(oompaLoompaResponse.totalPages, 1)
        assertEquals(oompaLoompaResponse.oompaLoompas.size, 1)
        assertEquals(oompaLoompa.id, 1)
        assertEquals(oompaLoompa.firstName, "John")
        assertEquals(oompaLoompa.age, 20)
    }

    @Test
    fun oompaLoompasLists_fetchOompaLoompaDetails() = runTest {
        val repository = DefaultOompaLoompasListRepository(FakeOompaLoompasListDatasource(), this)
        val oompaLoompaDetailsResponse = repository.fetchOompaLoompaDetails(1)
        val oompaLoompaFavorite = oompaLoompaDetailsResponse.favorite
        assertEquals(oompaLoompaDetailsResponse.firstName, "John")
        assertEquals(oompaLoompaDetailsResponse.lastName, "Doe")
        assertEquals(oompaLoompaDetailsResponse.profession, "Developer")
        assertEquals(oompaLoompaDetailsResponse.age, 21)
        assertEquals(oompaLoompaDetailsResponse.height, 150)
        assertEquals(oompaLoompaFavorite.color, "red")
        assertEquals(oompaLoompaFavorite.food, "pizza")
        assertEquals(oompaLoompaFavorite.randomString, "random")
    }

}

private class FakeOompaLoompasListDatasource : OompaLoompasDataSource {
    override suspend fun fetchOompaLoompas(page: Int): OompaLoompaResponse =
        OompaLoompaResponse(
            current = page, total = 1, results = listOf(
                OompaLoompaResultResponse(
                    id = 1,
                    firstName = "John",
                    lastName = "Doe",
                    profession = "Developer",
                    image = "image",
                    gender = "F",
                    age = 20,
                )
            )
        )

    override suspend fun fetchOompaLoompaDetails(id: Int): OompaLoompaDetailsResponse =
        OompaLoompaDetailsResponse(
            "John",
            "Doe",
            "M",
            "image",
            "Developer",
            21,
            "description",
            "quota",
            150,
            "country",
            "email",
            OompaLoompaFavoriteResponse(
                color = "red",
                food = "pizza",
                randomString = "random",
                song = "song",
            )
        )
}