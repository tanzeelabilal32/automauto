package com.mock.myanalytics.di

import com.mock.myanalytics.AnalyticsTracker
import com.mock.myanalytics.FBTracker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @Provides
    fun provideAnalyticsTracker(): AnalyticsTracker = FBTracker()
}