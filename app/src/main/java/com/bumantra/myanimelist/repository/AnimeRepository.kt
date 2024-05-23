package com.bumantra.myanimelist.repository

import com.bumantra.myanimelist.data.local.entity.FavoriteAnime
import com.bumantra.myanimelist.data.local.room.FavoriteAnimeDao
import com.bumantra.myanimelist.data.remote.response.DataItem
import com.bumantra.myanimelist.data.remote.retrofit.ApiService
import com.bumantra.myanimelist.util.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AnimeRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteAnimeDao,
) {
    private val animeList = mutableListOf<DataItem>()

    fun getAllAnime(): Flow<List<DataItem>> {
        wrapEspressoIdlingResource {
            return flow {
                try {
                    val response = apiService.getAnimeList().data
                    animeList.clear()
                    response.forEach {
                        animeList.add(it)
                    }
                    emit(animeList)
                } catch (e: Exception) {
                    throw e
                }
            }
        }
    }


    fun searchAnime(query: String): List<DataItem> {
        return animeList.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }

    fun getDetailById(id: Long): DataItem {
        return animeList.first {
            it.malId.toLong() == id
        }
    }

    suspend fun addToFavorites(anime: FavoriteAnime) {
        favoriteDao.insert(anime)
    }

    suspend fun deleteToFavorites(anime: FavoriteAnime) {
        favoriteDao.delete(anime)
    }

    suspend fun isFavorite(id: Int): Boolean {
        return favoriteDao.isFavorites(id)
    }

    fun getFavoriteAnime(): Flow<List<FavoriteAnime>> {
        return favoriteDao.getFavorites()
    }

    companion object {
        @Volatile
        private var instance: AnimeRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteAnimeDao
        ): AnimeRepository =
            instance ?: synchronized(this) {
                instance ?: AnimeRepository(apiService, favoriteDao)
            }.also { instance = it }
    }
}