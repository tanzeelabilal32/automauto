package com.sema.automauto.di

import com.sema.automauto.domain.usecase.getcars.GetCarsUseCase
import com.sema.automauto.domain.usecase.getcars.GetCarsUseCaseImpl
import com.sema.data.repository.CarsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UsecaseModule {
    @Singleton
    @Provides
    fun provideGetCarsUseCase(
        repository: CarsRepository,
        dispatcher: CoroutineDispatcher
    ): GetCarsUseCase {
        return GetCarsUseCaseImpl(repository, dispatcher)
    }
}