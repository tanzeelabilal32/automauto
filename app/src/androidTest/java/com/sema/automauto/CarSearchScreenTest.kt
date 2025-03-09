package com.sema.automauto

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sema.automauto.ui.screen.Tags
import com.sema.automauto.ui.screen.search.BindList
import com.sema.automauto.ui.screen.search.CarSearchScreen
import com.sema.shared_test.data.TestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class CarSearchScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testCarSearchScreen() {
        // given
        composeTestRule.activity.setContent {
            CarSearchScreen {
            }
        }

        // then
        val searchText = composeTestRule.activity.getString(R.string.cars_screen_search_hint)
        composeTestRule.onNodeWithText(searchText).assertIsDisplayed()
        composeTestRule.onNodeWithTag(Tags.FILTER).assertIsDisplayed()
    }

    @Test
    fun testItemViewsAreShownCorrectly() {
        // given
        var isImageClicked = false
        composeTestRule.activity.setContent {
            BindList(TestData.mockCars) {
                isImageClicked = true
            }
        }

        // then
        composeTestRule.onNodeWithTag(Tags.CARS_LIST_VIEW).onChildren().assertCountEquals(2)
        composeTestRule.onNodeWithTag(Tags.CARS_LIST_ITEM + 1).assertIsDisplayed()

        // when
        composeTestRule.onNodeWithTag(Tags.CARS_LIST_ITEM + 2).performClick()

        // then
        assertTrue(isImageClicked)
    }

    @Test
    fun testClickOnFilterIcon() {
        // given
        composeTestRule.activity.setContent {
            CarSearchScreen {
            }
        }

        // then
        composeTestRule.apply {
            onNodeWithTag(Tags.CARS_LIST_VIEW).onChildren()[0].assert(hasText("BMW 316i"))
            onNodeWithTag(Tags.CARS_LIST_VIEW).onChildren()[1].assert(hasText("Porsche 911"))
        }

        // when
        composeTestRule.onNodeWithTag(Tags.FILTER).performClick()

        // then
        composeTestRule.apply {
            onNodeWithTag(Tags.CARS_LIST_VIEW).onChildren().onFirst().assert(hasText("Porsche 911"))
            onNodeWithTag(Tags.CARS_LIST_VIEW).onChildren()[1].assert(hasText("BMW 316i"))
        }
    }
}