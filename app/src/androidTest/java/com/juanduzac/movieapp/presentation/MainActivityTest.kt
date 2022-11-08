package com.juanduzac.movieapp.presentation

import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.juanduzac.movieapp.presentation.movielist.composables.RecommendedMovieCardTestTag
import com.juanduzac.movieapp.presentation.navigation.Navigation
import com.juanduzac.movieapp.presentation.navigation.Screen
import com.juanduzac.movieapp.presentation.ui.theme.MovieAppTheme
import junit.framework.Assert
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private var actualRoute: String? = null

    @Before
    fun setUp() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            navController.addOnDestinationChangedListener { nvController, navDestination, bundle ->
                actualRoute = navDestination.route
            }
            MovieAppTheme() {
                Navigation(
                    navController = navController,
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun givenStartDestinationIsVendorListScreen_whenClickingAVendorItem_thenNavigateToVendorDetailScreen() {
        runTest {
            Assert.assertEquals(Screen.MovieListScreen.route, actualRoute)

            composeTestRule.onAllNodesWithTag(RecommendedMovieCardTestTag).assertAll(hasClickAction())
                .onFirst().performClick()

            composeTestRule.awaitIdle()
            Assert.assertEquals(Screen.MovieDetailScreen.route, actualRoute)
        }
    }


}