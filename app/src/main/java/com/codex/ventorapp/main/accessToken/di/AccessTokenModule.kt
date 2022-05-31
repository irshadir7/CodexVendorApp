package com.codex.ventorapp.main.accessToken.di



import com.codex.ventorapp.main.accessToken.network.AccessTokenApis
import com.codex.ventorapp.main.accessToken.repository.AccessTokenRepos
import com.codex.ventorapp.main.accessToken.vm.AccessTokenViewModels
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
object AccessTokenModules {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): AccessTokenApis {
        return retrofit.build()
            .create(AccessTokenApis::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        accessTokenApi: AccessTokenApis
    ): AccessTokenRepos {
        return AccessTokenRepos(accessTokenApi)
    }

    @Singleton
    @Provides
    fun provideAccessTokenViewModel(
        accessTokeRepo: AccessTokenRepos
    ): AccessTokenViewModels {
        return AccessTokenViewModels(accessTokeRepo)
    }
}