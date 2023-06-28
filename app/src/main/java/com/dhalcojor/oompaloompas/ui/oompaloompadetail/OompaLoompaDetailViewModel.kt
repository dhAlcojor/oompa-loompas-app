package com.dhalcojor.oompaloompas.ui.oompaloompadetail

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhalcojor.oompaloompas.data.OompaLoompasListRepository
import com.dhalcojor.oompaloompas.data.local.di.DefaultDispatcher
import com.dhalcojor.oompaloompas.data.local.models.OompaLoompaDetails
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
class OompaLoompaDetailViewModel @Inject constructor(
    private val oompaLoompasListRepository: OompaLoompasListRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        OompaLoompaDetailUiState(
            isLoading = false,
            userMessages = mutableStateListOf(),
            oompaLoompa = null
        )
    )
    val uiState: StateFlow<OompaLoompaDetailUiState> = _uiState.asStateFlow()
    private var fetchJob: Job? = null

    fun fetchOompaLoompaDetails(id: Int?) {
        if (id != null) {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    userMessages = mutableStateListOf(),
                    oompaLoompa = null
                )
            }

            fetchJob?.cancel()
            fetchJob = viewModelScope.launch {
                withContext(defaultDispatcher) {
                    _uiState.update { currentState ->
                        try {
                            val result = oompaLoompasListRepository.fetchOompaLoompaDetails(id)
                            currentState.copy(
                                isLoading = false,
                                userMessages = mutableStateListOf(),
                                oompaLoompa = result
                            )
                        } catch (e: IOException) {
                            currentState.copy(
                                isLoading = false,
                                userMessages = mutableStateListOf("Error fetching Oompa Loompa details"),
                                oompaLoompa = null
                            )
                        }
                    }
                }
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    userMessages = mutableStateListOf("Error: can't fetch details for null id"),
                    oompaLoompa = null
                )

            }
        }
    }

    companion object {
        private const val TAG = "OompaLoompaDetailViewModel"
    }
}

data class OompaLoompaDetailUiState(
    val isLoading: Boolean,
    val userMessages: List<String>,
    val oompaLoompa: OompaLoompaDetails?
)