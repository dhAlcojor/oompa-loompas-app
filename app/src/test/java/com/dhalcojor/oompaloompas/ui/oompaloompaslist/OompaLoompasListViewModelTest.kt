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

package com.dhalcojor.oompaloompas.ui.oompaloompaslist


import com.dhalcojor.oompaloompas.data.OompaLoompasListRepository
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaDetails
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class OompaLoompasListViewModelTest {
    @Test
    fun uiState_initialState() = runTest {
        val viewModel =
            OompaLoompasListViewModel(
                FakeOompaLoompasListRepository(),
                StandardTestDispatcher(testScheduler)
            )
        val uiState = viewModel.uiState.first()
        assertEquals(uiState.isLoading, false)
        assertEquals(uiState.currentPage, 1)
        assertEquals(uiState.totalPages, 1)
        assertEquals(uiState.oompaLoompasList.size, 0)
        assertEquals(uiState.userMessages.size, 0)
    }

    @Test
    fun uiState_onFetchPage() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val viewModel =
                OompaLoompasListViewModel(
                    FakeOompaLoompasListRepository(),
                    StandardTestDispatcher(testScheduler)
                )
            viewModel.fetchOompaLoompas(1)

            advanceUntilIdle()

            val uiState = viewModel.uiState.first()
            val oompaLoompa = uiState.oompaLoompasList[0]
            assertEquals(uiState.isLoading, false)
            assertEquals(uiState.currentPage, 1)
            assertEquals(uiState.totalPages, 20)
            assertEquals(uiState.oompaLoompasList.size, 1)
            assertEquals(uiState.userMessages.size, 0)
            assertEquals(oompaLoompa.id, 1)
        } finally {
            Dispatchers.resetMain()
        }
    }

    @Test
    fun uiState_onFetchError() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        try {
            val viewModel =
                OompaLoompasListViewModel(
                    FakeOompaLoompasListRepository(),
                    StandardTestDispatcher(testScheduler)
                )
            viewModel.fetchOompaLoompas(2)

            advanceUntilIdle()

            val uiState = viewModel.uiState.first()
            val userMessage = uiState.userMessages[0]
            assertEquals(uiState.isLoading, false)
            assertEquals(uiState.oompaLoompasList.size, 0)
            assertEquals(uiState.userMessages.size, 1)
            assertEquals(userMessage, "Error fetching oompa loompas")
        } finally {
            Dispatchers.resetMain()
        }
    }
}

private class FakeOompaLoompasListRepository : OompaLoompasListRepository {
    private var _oompaLoompaResult: OompaLoompaResult? = null
    override val oompaLoompaResult: OompaLoompaResult?
        get() = _oompaLoompaResult

    override suspend fun fetchOompaLoompas(page: Int, refresh: Boolean): OompaLoompaResult {
        return when (page) {
            1 -> OompaLoompaResult(
                1,
                20,
                listOf(OompaLoompa(1, "firstName", "lastName", "image", "profession", 21, "gender"))
            )

            2 -> throw IOException("Error fetching oompa loompas")

            else -> OompaLoompaResult(1, 1, listOf())
        }
    }

    override suspend fun fetchOompaLoompaDetails(id: Int): OompaLoompaDetails {
        TODO("Not yet implemented")
    }

}
