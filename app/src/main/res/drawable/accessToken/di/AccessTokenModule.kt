package com.demo.ventorapp.main.accessToken.di


import com.codex.ventorapp.main.accessToken.network.AccessTokenApi
import com.codex.ventorapp.main.accessToken.repository.AccessTokenRepo
import com.codex.ventorapp.main.accessToken.vm.AccessTokenViewModel
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
object AccessTokenModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): AccessTokenApi {
        return retrofit.build()
            .create(AccessTokenApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        accessTokenApi: AccessTokenApi
    ): AccessTokenRepo {
        return AccessTokenRepo(accessTokenApi)
    }

    @Singleton
    @Provides
    fun provideCountryViewModel(
        accessTokeRepo: AccessTokenRepo
    ): AccessTokenViewModel {
        return AccessTokenViewModel(accessTokeRepo)
    }
}