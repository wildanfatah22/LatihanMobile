package com.example.moviesapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.moviesapp.model.MovieData
import com.example.moviesapp.presentation.navigation.Screen
import com.example.moviesapp.ui.theme.MoviesAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SubmissionComposeKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MoviesAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                MoviesApp(navController = navController)
            }
        }
    }

    @Test
    fun navHost_verifyStartDestination() {
        navController.assertCurrentRouteName(Screen.Home.route)
    }
    
    @Test
    fun navHost_clickItem_navigatesToDetailWithData() {
        composeTestRule.onNodeWithTag("lazy_list").performScrollToIndex(5)
        composeTestRule.onNodeWithText(MovieData.dummyMovies[5].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithText(MovieData.dummyMovies[5].title).assertIsDisplayed()
    }
    
    @Test
    fun navHost_bottomNavigation_working() {
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.menu_home).performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
    }
    
    @Test
    fun navigateTo_ProfilePage() {
        composeTestRule.onNodeWithStringId(R.string.menu_profile).performClick()
        navController.assertCurrentRouteName(Screen.Profile.route)
        composeTestRule.onNodeWithStringId(R.string.my_name).assertIsDisplayed()
        composeTestRule.onNodeWithStringId(R.string.my_email).assertIsDisplayed()
    }
    
    @Test
    fun searchShowEmptyListMovie() {
        val incorrectSearch = "asdas"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(incorrectSearch)
        composeTestRule.onNodeWithTag("emptyList").assertIsDisplayed()
    }

    @Test
    fun searchShowListMovie() {
        val rightSearch = "kevin"
        composeTestRule.onNodeWithStringId(R.string.search_text).performTextInput(rightSearch)
        composeTestRule.onNodeWithText("kevin").assertIsDisplayed()
    }
    
    @Test
    fun favoriteClickInDetailScreen_ShowInFavoriteScreen() {
        composeTestRule.onNodeWithText(MovieData.dummyMovies[0].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithText(MovieData.dummyMovies[0].title).assertIsDisplayed()
    }
    
    @Test
    fun favoriteClickAndDeleteFavoriteInDetailScreen_NotShowInFavoriteScreen() {
        composeTestRule.onNodeWithText(MovieData.dummyMovies[0].title).performClick()
        navController.assertCurrentRouteName(Screen.DetailMovie.route)
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("favorite_detail_button").performClick()
        composeTestRule.onNodeWithTag("back_home").performClick()
        navController.assertCurrentRouteName(Screen.Home.route)
        composeTestRule.onNodeWithStringId(R.string.menu_favorite).performClick()
        navController.assertCurrentRouteName(Screen.Favorite.route)
        composeTestRule.onNodeWithStringId(R.string.empty_data).assertIsDisplayed()
    }


}