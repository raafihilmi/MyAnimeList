package com.bumantra.myanimelist.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumantra.myanimelist.data.local.entity.FavoriteAnime

@Database(entities = [FavoriteAnime::class], version = 1)
abstract class FavoriteAnimeDatabase : RoomDatabase() {
    abstract fun favoriteAnimeDao(): FavoriteAnimeDao

    companion object {
        @Volatile
        private var instance: FavoriteAnimeDatabase? = null
        fun getInstance(context: Context): FavoriteAnimeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteAnimeDatabase::class.java, "favorites.db"
                ).build()
            }
    }
}