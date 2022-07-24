package com.codex.ventorapp.main.purchase.di

import com.codex.ventorapp.main.login.network.LoginApi
import com.codex.ventorapp.main.login.repository.LoginRepo
import com.codex.ventorapp.main.login.vm.LoginViewModel
import com.codex.ventorapp.main.purchase.network.PurchaseApi
import com.codex.ventorapp.main.purchase.repository.PurchaseRepo
import com.codex.ventorapp.main.purchase.vm.PurchaseViewModel
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
object PurchaseModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): PurchaseApi {
        return retrofit.build()
            .create(PurchaseApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        purchaseApi: PurchaseApi
    ): PurchaseRepo {
        return PurchaseRepo(purchaseApi)
    }

    @Singleton
    @Provides
    fun provideViewModel(
        purchaseRepo: PurchaseRepo
    ): PurchaseViewModel {
        return PurchaseViewModel(purchaseRepo)
    }
}