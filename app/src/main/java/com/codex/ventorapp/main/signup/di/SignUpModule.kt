package com.codex.ventorapp.main.signup.di

import com.codex.ventorapp.main.signup.network.SignupApi
import com.codex.ventorapp.main.signup.repository.SignupRepo
import com.codex.ventorapp.main.signup.vm.SignUpViewModel
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
object SignUpModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): SignupApi {
        return retrofit.build()
            .create(SignupApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        signUpApi: SignupApi
    ): SignupRepo {
        return SignupRepo(signUpApi)
    }

    @Singleton
    @Provides
    fun provideSignUpViewModel(
        signupRepo: SignupRepo
    ): SignUpViewModel {
        return SignUpViewModel(signupRepo)
    }
}