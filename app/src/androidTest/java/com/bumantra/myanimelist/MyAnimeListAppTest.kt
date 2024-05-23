package com.bumantra.myanimelist

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.printToLog
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.espresso.IdlingRegistry
import com.bumantra.myanimelist.data.remote.retrofit.ApiConfig
import com.bumantra.myanimelist.ui.navigation.Screen
import com.bumantra.myanimelist.ui.theme.MyAnimeListTheme
import com.bumantra.myanimelist.util.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MyAnimeListAppTest {
    private val mockWebServer = MockWebServer()

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController


    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        composeTestRule.setContent {
            MyAnimeListTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MyAnimeListApp(navController = navController)
            }
        }
        composeTestRule.onRoot().printToLog("currentLabelExist")
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun getAnimeList_Success() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        composeTestRule.waitUntilDoesNotExist(hasTestTag("Load"))
        composeTestRule.onNodeWithTag("AnimeList").assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun getAnimeList_Error() {
        val mockResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockResponse)


        composeTestRule.waitUntilDoesNotExist(hasTestTag("Load"))
        composeTestRule.onNodeWithTag("HomeError").assertIsDisplayed()
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navHost_clickItem_navigateToDetail() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        composeTestRule.waitUntilDoesNotExist(hasTestTag("Load"))
        composeTestRule.onNodeWithTag("AnimeList").performScrollToIndex(10).performClick()
        navController.assertCurrentRouteName(Screen.DetailAnime.route)
        composeTestRule.onNodeWithStringId(R.string.item_anime).assertIsDisplayed()
    }

    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithText("Favorite").performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText("About").performClick()
        navController.assertCurrentRouteName(Screen.About.route)
        composeTestRule.onNodeWithText("Home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navHost_clickItem_navigatesBack() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("success_response.json"))
        mockWebServer.enqueue(mockResponse)

        composeTestRule.waitUntilDoesNotExist(hasTestTag("Load"))
        composeTestRule.onNodeWithTag("AnimeList").performScrollToIndex(10).performClick()
        navController.assertCurrentRouteName(Screen.DetailAnime.route)
        composeTestRule.onNodeWithContentDescription(composeTestRule.activity.getString(R.string.back_to_home))
            .performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }


}