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

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhalcojor.oompaloompas.data.OompaLoompasListRepository
import com.dhalcojor.oompaloompas.data.local.di.DefaultDispatcher
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OompaLoompasListViewModel @Inject constructor(
    private val oompaLoompasListRepository: OompaLoompasListRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OompaLoompasListUiState(
            oompaLoompasList = mutableStateListOf(),
            userMessages = mutableStateListOf()
        )
    )
    val uiState: StateFlow<OompaLoompasListUiState> = _uiState.asStateFlow()
    private var fetchJob: Job? = null

    fun fetchOompaLoompas(page: Int = 1) {
        Log.d(TAG, "fetchOompaLoompas: page = $page")
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true,
                userMessages = mutableStateListOf(),
                oompaLoompasList = mutableStateListOf()
            )
        }
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            withContext(defaultDispatcher) {
                _uiState.update { currentState ->
                    try {
                        val result = oompaLoompasListRepository.fetchOompaLoompas(
                            page,
                            page != currentState.currentPage
                        )
                        Log.d(
                            TAG,
                            "fetchOompaLoompas: retrieved ${result.oompaLoompas.size} results $result.oompaLoompas"
                        )
                        currentState.copy(
                            currentPage = result.currentPage,
                            totalPages = result.totalPages,
                            isLoading = false,
                            userMessages = mutableStateListOf(),
                            oompaLoompasList = result.oompaLoompas
                        )
                    } catch (ioe: IOException) {
                        val messages = getMessagesFromThrowable(ioe)
                        Log.e(TAG, "fetchOompaLoompas: error = $messages")
                        currentState.copy(
                            isLoading = false,
                            userMessages = messages,
                            oompaLoompasList = mutableStateListOf()
                        )
                    }

                }
            }
        }
    }

    companion object {
        private const val TAG = "OompaLoompasListVM"
    }
}

fun getMessagesFromThrowable(throwable: Throwable): List<String> {
    val messages = mutableListOf<String>()
    var currentThrowable: Throwable? = throwable
    while (currentThrowable != null) {
        messages.add(currentThrowable.message ?: currentThrowable.toString())
        currentThrowable = currentThrowable.cause
    }
    return messages
}

data class OompaLoompasListUiState(
    val currentPage: Int = 1,
    val totalPages: Int = 1,
    val isLoading: Boolean = false,
    val oompaLoompasList: List<OompaLoompa> = mutableStateListOf(),
    val userMessages: List<String> = mutableStateListOf()
)
