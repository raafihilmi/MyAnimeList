package com.bumantra.myanimelist.data.remote.retrofit

import com.bumantra.myanimelist.data.remote.response.AnimeResponse
import retrofit2.http.GET

interface ApiService {
    @GET("anime")
    suspend fun getAnimeList(): AnimeResponse

}