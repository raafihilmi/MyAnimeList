package com.bumantra.myanimelist.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object About : Screen("about")
    object DetailAnime : Screen("home/{animeId}") {
        fun createRoute(animeId: Long) = "home/$animeId"
    }
}
