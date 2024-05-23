package com.bumantra.myanimelist.ui.screen.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.bumantra.myanimelist.data.remote.response.DataItem
import com.bumantra.myanimelist.data.remote.response.Images
import com.bumantra.myanimelist.data.remote.response.Jpg
import com.bumantra.myanimelist.ui.theme.MyAnimeListTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailContentTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    private val fakeAnimeList = DataItem(
        rating = "PG - Children",
        title = "Bouken Ou Beet",
        images = Images(Jpg("https://cdn.myanimelist.net/images/anime/7/21569.jpg")),
        malId = 0,
        synopsis = "Is is the dark"
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MyAnimeListTheme {
                DetailContent(
                    image = fakeAnimeList.images,
                    title = fakeAnimeList.title,
                    rating = fakeAnimeList.rating,
                    synopsis = fakeAnimeList.synopsis,
                    onBackClick = { /*TODO*/ },
                    isFavorite = false,
                    onFavoriteClick = { /*TODO*/ }
                )
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExist")
    }

    @Test
    fun detailContent_isDisplayed() {
        composeTestRule.onNodeWithText(fakeAnimeList.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeAnimeList.rating).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeAnimeList.synopsis).assertIsDisplayed()
    }

}