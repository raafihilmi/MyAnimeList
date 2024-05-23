package com.bumantra.myanimelist.ui.screen.favorite

import androidx.lifecycle.ViewModel
import com.bumantra.myanimelist.repository.AnimeRepository

class FavoriteViewModel(repository: AnimeRepository) : ViewModel() {
    val getFavoriteList = repository.getFavoriteAnime()
}