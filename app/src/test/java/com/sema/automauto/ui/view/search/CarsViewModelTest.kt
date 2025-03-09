package com.sema.automauto.ui.view.search

import com.sema.automauto.domain.usecase.getcars.GetCarsUseCase
import com.sema.automauto.ui.screen.search.CarsListUiState
import com.sema.automauto.ui.screen.search.CarsViewModel
import com.sema.data.common.Resource
import com.sema.data.common.Resource.Companion.success
import com.sema.shared_test.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.whenever

/**
 * Unit tests for the [CarsViewModel].
 */
@ExperimentalCoroutinesApi
class CarsViewModelTest {

    @Mock
    lateinit var getCarsUseCase: GetCarsUseCase

    private lateinit var viewModel: CarsViewModel

    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        openMocks(this)
        viewModel = CarsViewModel(getCarsUseCase)
    }

    @Test
    fun `when getcars api is called, should emit a loading and success state`() = runTest {
        // given
        whenever(getCarsUseCase()).thenReturn(flowOf(success(listOf())))

        // when
        viewModel.getCars()

        // then
        assertTrue(
            viewModel.carsUiState.value is CarsListUiState.CarsListUiStateReady
        )
    }

    @Test
    fun `when getcars api throws an error, should emit an error state`() = runTest {
        // given
        whenever(getCarsUseCase()).thenReturn(
            flowOf(Resource.error(Throwable("Something went wrong")))
        )

        // when
        viewModel.getCars()

        // then
        assertTrue(viewModel.carsUiState.value is CarsListUiState.CarsListUiStateError)
    }
}