package com.sema.data.repository

import com.sema.data.api.ApiService
import com.sema.data.model.CarSearchItem
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

interface CarsRepository {
    suspend fun getCars(): List<CarSearchItem>
}

@ActivityRetainedScoped
class CarsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
) : CarsRepository {
    override suspend fun getCars(): List<CarSearchItem> = apiService.getCars()
}