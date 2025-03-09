package com.sema.automauto.domain.usecase.getcars

import com.sema.automauto.domain.util.FilterType
import com.sema.automauto.domain.util.SortingType
import com.sema.data.common.Resource
import com.sema.data.model.CarSearchItem
import com.sema.data.repository.CarsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * Domain related business logic
 */

interface GetCarsUseCase {
    suspend operator fun invoke(): Flow<Resource<List<CarSearchItem>>>

    suspend operator fun invoke(
        query: String,
        filterType: FilterType = FilterType.Date(SortingType.Descending)
    ): Flow<Resource<List<CarSearchItem>>>
}

class GetCarsUseCaseImpl @Inject constructor(
    private val repository: CarsRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : GetCarsUseCase {
    private var carList = listOf<CarSearchItem>()

    override suspend operator fun invoke(): Flow<Resource<List<CarSearchItem>>> =
        flow {
            try {
                emit(Resource.loading())
                carList = repository.getCars()
                emit(Resource.success(carList))
            } catch (e: Throwable) {
                emit(Resource.error(e))
            }
        }.flowOn(defaultDispatcher)

    override suspend operator fun invoke(
        query: String,
        filterType: FilterType
    ): Flow<Resource<List<CarSearchItem>>> =
        filter(flowOf(carList), query, filterType)
            .onStart {
                emit(Resource.loading())
            }
            .catch {
                emit(Resource.error(Throwable("Something went wrong")))
            }.flowOn(defaultDispatcher)

    private fun filter(
        response: Flow<List<CarSearchItem>>,
        query: String,
        filterType: FilterType = FilterType.Date(SortingType.Descending)
    ): Flow<Resource<List<CarSearchItem>>> {
        val filter = response.map {
            it.filter { car -> car.make.lowercase().contains(query.lowercase()) }
        }
        return filter.map { cars ->
            val sortedCars = when (filterType.sortingType) {
                is SortingType.Ascending -> {
                    when (filterType) {
                        is FilterType.Title -> cars.sortedBy { it.model.lowercase() }
                        is FilterType.Date -> cars.sortedBy { it.firstRegistration }
                        is FilterType.Color -> cars.sortedBy { it.colour }
                        is FilterType.Price -> cars.sortedBy { it.price }
                    }
                }

                is SortingType.Descending -> {
                    when (filterType) {
                        is FilterType.Title -> cars.sortedByDescending { it.model.lowercase() }
                        is FilterType.Date -> cars.sortedByDescending { it.firstRegistration }
                        is FilterType.Color -> cars.sortedByDescending { it.colour }
                        is FilterType.Price -> cars.sortedByDescending { it.price }
                    }
                }
            }
            Resource.success(sortedCars)
        }
    }
}

