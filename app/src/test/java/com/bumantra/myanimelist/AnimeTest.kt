package com.bumantra.myanimelist

import org.junit.Assert.*
import org.junit.Test

class AnimeTest  {
    @Test
    fun testAnimeModelSuccess() {
        val anime = Anime()
        anime.id = 1
        anime.title = "Naruto"

        assertEquals(1, anime.id)
        assertEquals("Naruto", anime.title)
    }

    @Test
    fun testAnimeModelFailure() {
        val anime = Anime()
        anime.id = 2
        anime.title = "One Piece"

        // Tes ini akan gagal karena nilainya tidak sesuai dengan yang diharapkan
        assertEquals(1, anime.id)
        assertEquals("Naruto", anime.title)
    }
}