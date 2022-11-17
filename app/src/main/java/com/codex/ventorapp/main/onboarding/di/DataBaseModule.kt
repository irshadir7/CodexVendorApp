package com.codex.ventorapp.main.onboarding.di

import com.codex.ventorapp.main.login.network.LoginApi
import com.codex.ventorapp.main.login.repository.LoginRepo
import com.codex.ventorapp.main.login.vm.LoginViewModel
import com.codex.ventorapp.main.onboarding.network.DatabaseApi
import com.codex.ventorapp.main.onboarding.repo.DatabaseRepo
import com.codex.ventorapp.main.onboarding.vm.DatabaseViewModel
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
object DataBaseModule {
    @Singleton
    @Provides
    fun provideDatabase(retrofit: Retrofit.Builder): DatabaseApi {
        return retrofit.build()
            .create(DatabaseApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDatabaseRepo(
        databaseApi: DatabaseApi
    ): DatabaseRepo {
        return DatabaseRepo(databaseApi)
    }

    @Singleton
    @Provides
    fun provideDataBaseViewModel(
        databaseRepo: DatabaseRepo
    ): DatabaseViewModel {
        return DatabaseViewModel(databaseRepo)
    }
}