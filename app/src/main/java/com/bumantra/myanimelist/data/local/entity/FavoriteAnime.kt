package com.bumantra.myanimelist.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteAnime(
    @PrimaryKey
    val id: Int,
    val image: String,
    val title: String,
    val rating: String,
)
