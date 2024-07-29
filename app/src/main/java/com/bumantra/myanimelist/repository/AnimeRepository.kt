package com.bumantra.myanimelist.repository

import android.util.Log
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

    /**
     * Mengambil semua data anime dari API dan mengembalikannya dalam bentuk aliran data.
     *
     * @return Flow<List<DataItem>> Aliran daftar objek DataItem yang mewakili data anime.
     * @throws Exception jika terjadi kesalahan saat mengambil data dari API.
     */
    fun getAllAnime(): Flow<List<DataItem>> {
        wrapEspressoIdlingResource {
            return flow {
                try {
                    Log.d("getAllAnime", "Fetching anime list from API")
                    val response = apiService.getAnimeList().data

                    Log.d("getAllAnime", "Clearing the existing anime list")
                    animeList.clear()

                    Log.d("getAllAnime", "Adding fetched anime to the list")
                    response.forEach {
                        animeList.add(it)
                    }

                    Log.d("getAllAnime", "Emitting anime list with size: ${animeList.size}")
                    emit(animeList)
                } catch (e: Exception) {
                    Log.e("getAllAnime", "Error fetching anime list", e)
                    throw e
                }
            }
        }
    }

    /**
     * Mencari anime dalam daftar berdasarkan judul.
     *
     * @param query String yang digunakan sebagai kriteria pencarian.
     * @return List<DataItem> Daftar anime yang cocok dengan query pencarian.
     */
    fun searchAnime(query: String): List<DataItem> {
        return animeList.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }

    /**
     * Mengambil detail anime berdasarkan ID yang diberikan.
     *
     * @param id Long ID dari anime yang ingin diambil detailnya.
     * @return DataItem Objek DataItem yang mewakili detail anime.
     */
    fun getDetailById(id: Long): DataItem {
        return animeList.first {
            it.malId.toLong() == id
        }
    }

    /**
     * Menambahkan anime ke daftar favorit.
     *
     * @param anime FavoriteAnime Objek FavoriteAnime yang akan ditambahkan ke daftar favorit.
     */
    suspend fun addToFavorites(anime: FavoriteAnime) {
        favoriteDao.insert(anime)
    }

    /**
     * Menghapus anime dari daftar favorit.
     *
     * @param anime FavoriteAnime Objek FavoriteAnime yang akan dihapus dari daftar favorit.
     */
    suspend fun deleteToFavorites(anime: FavoriteAnime) {
        favoriteDao.delete(anime)
    }

    /**
     * Mengecek apakah sebuah anime termasuk dalam daftar favorit berdasarkan ID.
     *
     * @param id Int ID dari anime yang akan dicek.
     * @return Boolean True jika anime ada dalam daftar favorit, False jika tidak.
     */
    suspend fun isFavorite(id: Int): Boolean {
        return favoriteDao.isFavorites(id)
    }

    /**
     * Mengambil semua anime favorit dalam bentuk aliran data.
     *
     * @return Flow<List<FavoriteAnime>> Aliran daftar objek FavoriteAnime yang mewakili data anime favorit.
     */
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