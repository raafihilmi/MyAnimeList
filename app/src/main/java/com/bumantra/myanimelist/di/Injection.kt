package com.bumantra.myanimelist.di

import android.content.Context
import com.bumantra.myanimelist.data.local.room.FavoriteAnimeDatabase
import com.bumantra.myanimelist.data.remote.retrofit.ApiConfig
import com.bumantra.myanimelist.repository.AnimeRepository

object Injection {
    fun provideRepository(context: Context): AnimeRepository{
        val db = FavoriteAnimeDatabase.getInstance(context)
        val dao = db.favoriteAnimeDao()
        val apiService = ApiConfig.getApiService()
        return AnimeRepository.getInstance(apiService, dao)
    }
}