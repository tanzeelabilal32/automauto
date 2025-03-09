package com.sema.automauto.di

import com.sema.data.api.ApiService
import com.sema.data.repository.CarsRepository
import com.sema.data.repository.CarsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Singleton
    @Provides
    fun provideCarsRepository(apiService: ApiService): CarsRepository {
        return CarsRepositoryImpl(apiService)
    }
}