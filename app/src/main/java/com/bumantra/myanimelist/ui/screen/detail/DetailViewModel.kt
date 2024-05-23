package com.bumantra.myanimelist.ui.screen.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumantra.myanimelist.data.local.entity.FavoriteAnime
import com.bumantra.myanimelist.data.remote.response.DataItem
import com.bumantra.myanimelist.repository.AnimeRepository
import com.bumantra.myanimelist.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: AnimeRepository) : ViewModel() {
    private var isFavorite: Boolean by mutableStateOf(false)
    private val _uiState: MutableStateFlow<UiState<DataItem>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<DataItem>>
        get() = _uiState

    fun getDetailById(id: Long) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getDetailById(id))
        }
    }

    fun isFavoriteAnime(id: Int): Boolean {
        viewModelScope.launch {
            isFavorite = repository.isFavorite(id)
        }
        return isFavorite
    }

    fun toggleFavoriteStatus(anime: FavoriteAnime) {
        isFavorite = !isFavorite

        viewModelScope.launch {
            if (isFavorite) {
                repository.addToFavorites(anime)
            } else {
                repository.deleteToFavorites(anime)
            }
        }
    }


}