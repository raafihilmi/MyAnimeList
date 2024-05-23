package com.bumantra.myanimelist.ui.screen.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumantra.myanimelist.data.remote.response.DataItem
import com.bumantra.myanimelist.repository.AnimeRepository
import com.bumantra.myanimelist.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: AnimeRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<List<DataItem>>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<DataItem>>>
        get() = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getAllListAnime() {
        viewModelScope.launch {
            _query.value = ""
            repository.getAllAnime().catch {
                _uiState.value = UiState.Error(it.message.toString())
            }.collect { data ->
                _uiState.value = UiState.Success(data.sortedBy { it.title })
            }
        }
    }

    fun search(newQuery: String) {
        _query.value = newQuery
        _uiState.value = UiState.Success(
            repository.searchAnime(_query.value).sortedBy { it.title }
        )
    }
}