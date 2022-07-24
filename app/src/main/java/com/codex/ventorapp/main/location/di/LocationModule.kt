package com.codex.ventorapp.main.location.di

import com.codex.ventorapp.main.location.network.LocationApi
import com.codex.ventorapp.main.location.repository.LocationRepo
import com.codex.ventorapp.main.location.vm.LocationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Retrofit
import javax.inject.Singleton

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): LocationApi {
        return retrofit.build()
            .create(LocationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        locationApi: LocationApi
    ): LocationRepo {
        return LocationRepo(locationApi)
    }

    @Singleton
    @Provides
    fun provideLocationViewModel(
        locationRepo: LocationRepo
    ): LocationViewModel {
        return LocationViewModel(locationRepo)
    }
}