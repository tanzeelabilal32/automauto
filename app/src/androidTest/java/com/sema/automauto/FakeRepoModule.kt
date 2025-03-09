package com.sema.automauto

import com.sema.automauto.di.RepoModule
import com.sema.data.api.ApiService
import com.sema.data.model.CarSearchItem
import com.sema.data.repository.CarsRepository
import com.sema.shared_test.data.TestData
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepoModule::class]
)
@Module
class FakeRepoModule {

    @Singleton
    @Provides
    fun provideCarsRepository(apiService: ApiService) = object : CarsRepository {
        override suspend fun getCars(): List<CarSearchItem> {
            return TestData.mockCars
        }
    }
}