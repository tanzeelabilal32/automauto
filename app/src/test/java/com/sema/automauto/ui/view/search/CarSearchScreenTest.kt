package com.sema.automauto.ui.view.search

import android.os.Build
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sema.automauto.domain.usecase.getcars.GetCarsUseCase
import com.sema.automauto.ui.screen.Tags
import com.sema.automauto.ui.screen.search.BindList
import com.sema.automauto.ui.screen.search.CarSearchScreen
import com.sema.automauto.ui.screen.search.CarsViewModel
import com.sema.data.common.Resource
import com.sema.shared_test.data.TestData
import com.sema.shared_test.util.MainDispatcherRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(
    sdk = [Build.VERSION_CODES.P], application = HiltTestApplication::class,
    instrumentedPackages = [
        // required to access final members on androidx.loader.content.ModernAsyncTask
        "androidx.loader.content"
    ]
)
class CarSearchScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @RelaxedMockK
    private lateinit var carsUseCase: GetCarsUseCase

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        ShadowLog.stream = System.out // redirect Logcat
    }

    @Test
    fun testCarSearchScreen() {
        // given
        coEvery { carsUseCase.invoke() } returns flowOf(Resource.success(TestData.mockCars))

        val carsViewModel = CarsViewModel(carsUseCase)
        composeTestRule.apply {
            // when
            setContent {
                CarSearchScreen(carsViewModel) {
                }
            }
            // then
            onNodeWithText(Tags.SEARCH).assertIsDisplayed()
            onNodeWithTag(Tags.FILTER).assertExists()
        }
    }

    @Test
    fun testItemViewsAreShownCorrectly() {

    }

    @Test
    fun testClickOnFilterIcon() {

    }

    @Test
    fun testSearch() {

    }

    @Test
    fun testClickOnItemView() {

    }
}