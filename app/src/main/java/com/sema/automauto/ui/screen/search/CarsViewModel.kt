package com.sema.automauto.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sema.automauto.domain.usecase.getcars.GetCarsUseCase
import com.sema.automauto.domain.util.FilterType
import com.sema.automauto.domain.util.SortingType
import com.sema.data.common.Resource
import com.sema.data.model.CarSearchItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI related business logic
 */
@HiltViewModel
class CarsViewModel @Inject constructor(
    private val carsUseCase: GetCarsUseCase,
) : ViewModel() {
    private val _carsUiState = MutableStateFlow<CarsListUiState>(CarsListUiState.Loading)
    val carsUiState: StateFlow<CarsListUiState> = _carsUiState

    private var filterType: FilterType = FilterType.Date(SortingType.Descending)
    private var queryString: String = ""

    init {
        getCars()
    }

    fun searchCars(query: String) = viewModelScope.launch {
        queryString = query
        carsUseCase(queryString, filterType).collect(::handleResponse)
    }

    fun filterCars(filterType: FilterType) = viewModelScope.launch {
        this@CarsViewModel.filterType = filterType
        carsUseCase(queryString, filterType).collect(::handleResponse)
    }

    fun getCars() = viewModelScope.launch {
        carsUseCase().collect(::handleResponse)
    }

    private fun handleResponse(it: Resource<List<CarSearchItem>>) =
        when (it.status) {
            Resource.Status.LOADING -> _carsUiState.value = CarsListUiState.Loading

            Resource.Status.SUCCESS -> _carsUiState.value =
                CarsListUiState.CarsListUiStateReady(cars = it.data)

            Resource.Status.ERROR -> _carsUiState.value =
                CarsListUiState.CarsListUiStateError(error = it.error?.data?.message)
        }
}

sealed class CarsListUiState {
    data class CarsListUiStateReady(val cars: List<CarSearchItem>?) : CarsListUiState()
    object Loading : CarsListUiState()
    data class CarsListUiStateError(val error: String? = null) : CarsListUiState()
}