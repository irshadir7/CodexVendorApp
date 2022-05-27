package com.codex.ventorapp.main.login.di

import com.codex.ventorapp.main.accessToken.network.AccessTokenApis
import com.codex.ventorapp.main.accessToken.repository.AccessTokenRepos
import com.codex.ventorapp.main.accessToken.vm.AccessTokenViewModels
import com.codex.ventorapp.main.login.network.LoginApi
import com.codex.ventorapp.main.login.repository.LoginRepo
import com.codex.ventorapp.main.login.vm.LoginViewModel
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
object LoginModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): LoginApi {
        return retrofit.build()
            .create(LoginApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        accessTokenApi: LoginApi
    ): LoginRepo {
        return LoginRepo(accessTokenApi)
    }

    @Singleton
    @Provides
    fun provideLoginViewModel(
        loginRepo: LoginRepo
    ): LoginViewModel {
        return LoginViewModel(loginRepo)
    }
}