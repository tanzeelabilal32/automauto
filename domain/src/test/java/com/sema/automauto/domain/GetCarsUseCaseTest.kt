package com.sema.automauto.domain

import com.sema.automauto.domain.usecase.getcars.GetCarsUseCase
import com.sema.automauto.domain.usecase.getcars.GetCarsUseCaseImpl
import com.sema.data.common.Resource
import com.sema.data.repository.CarsRepository
import com.sema.shared_test.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.openMocks
import org.mockito.kotlin.whenever

/**
 * Unit tests for the [GetCarsUseCaseTest].
 */
@ExperimentalCoroutinesApi
class GetCarsUseCaseTest {

    @Mock
    lateinit var repository: CarsRepository

    private lateinit var getCarsUseCase: GetCarsUseCase

    @get:Rule
    var coroutineRule = MainDispatcherRule()

    @Before
    fun setUp() {
        openMocks(this)
        getCarsUseCase = GetCarsUseCaseImpl(repository, coroutineRule.testDispatcher)
    }

    @Test
    fun `when getcars api is called, should emit a loading and success state`() = runTest {
        // given
        whenever(repository.getCars()).thenReturn(listOf())

        // when
        val actual = mutableListOf<Resource.Status>()
        getCarsUseCase().collect {
            actual.add(it.status)
        }

        // then
        assertEquals(
            listOf(Resource.Status.LOADING, Resource.Status.SUCCESS), actual
        )
    }

    @Test
    fun `when getcars api returns an error, should emit an error state`() = runTest {

    }

    @Test
    fun `when typing a search query, should emit searched result`() = runTest {

    }

    @Test
    fun `when typing a search query, should emit empty searched result`() = runTest {

    }

    @Test
    fun `when 'filter' is clicked, should emit reversed cars result`() = runTest {

    }
}
