package com.example.moviesapp.presentation.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import com.example.moviesapp.R
import com.example.moviesapp.model.Movie
import com.example.moviesapp.ui.theme.MoviesAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailScreenKtTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val fakeDataMovie = Movie(
        id = 1,
        title = "The Batman (2022)",
        description = "In his second year of fighting crime, Batman uncovers corruption in Gotham City that connects to his own family while facing a serial killer known as the Riddler.",
        image = R.drawable.img,
        rating = 4.5,
        isFavorite = false,
    )

    @Before
    fun setUp() {
        composeTestRule.setContent {
            MoviesAppTheme {
                DetailInformation(
                    id = fakeDataMovie.id,
                    title = fakeDataMovie.title,
                    description = fakeDataMovie.description,
                    image = fakeDataMovie.image,
                    rating = fakeDataMovie.rating,
                    isFavorite = fakeDataMovie.isFavorite,
                    navigateBack = {},
                    onFavoriteButtonClicked = {_, _ ->}
                )
            }
        }
    }

    @Test
    fun detailInformation_isDisplayed() {
        composeTestRule.onNodeWithTag("scrollToBottom").performTouchInput {
            swipeUp()
        }
        composeTestRule.onNodeWithText(fakeDataMovie.title).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDataMovie.description).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDataMovie.rating.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(fakeDataMovie.description).assertIsDisplayed()
    }

    @Test
    fun addToFavoriteButton_hasClickAction() {
        composeTestRule.onNodeWithTag("favorite_detail_button").assertHasClickAction()
    }

    @Test
    fun detailInformation_isScrollable() {
        composeTestRule.onNodeWithTag("scrollToBottom").performTouchInput {
            swipeUp()
        }
    }

    @Test
    fun favoriteButton_hasCorrectStatus() {
        // Assert that the favorite button is displayed
        composeTestRule.onNodeWithTag("favorite_detail_button").assertIsDisplayed()

        // Assert that the content description of the favorite button is correct based on the isFavorite state
        val isFavorite = fakeDataMovie.isFavorite // Set the isFavorite state here
        val expectedContentDescription = if (isFavorite) {
            "Remove from Favorite"
        } else {
            "Add to Favorite"
        }

        composeTestRule.onNodeWithTag("favorite_detail_button")
            .assertContentDescriptionEquals(expectedContentDescription)
    }
}