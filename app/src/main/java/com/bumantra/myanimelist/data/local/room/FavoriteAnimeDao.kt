package com.bumantra.myanimelist.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bumantra.myanimelist.data.local.entity.FavoriteAnime
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAnimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteAnime)

    @Delete
    suspend fun delete(favorite: FavoriteAnime)

    @Query("SELECT EXISTS(SELECT * FROM favorites WHERE id = :id)")
    suspend fun isFavorites(id: Int): Boolean

    @Query("SELECT * from favorites")
    fun getFavorites(): Flow<List<FavoriteAnime>>
}