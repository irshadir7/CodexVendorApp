package com.codex.ventorapp.main.returns.di

import com.codex.ventorapp.main.receipt.network.ReceiptApi
import com.codex.ventorapp.main.receipt.repository.ReceiptRepo
import com.codex.ventorapp.main.receipt.vm.ReceiptViewModel
import com.codex.ventorapp.main.returns.network.ReturnApi
import com.codex.ventorapp.main.returns.repository.ReturnRepo
import com.codex.ventorapp.main.returns.vm.ReturnViewModel
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
object ReturnModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): ReturnApi {
        return retrofit.build()
            .create(ReturnApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        returnApi: ReturnApi
    ): ReturnRepo {
        return ReturnRepo(returnApi)
    }

    @Singleton
    @Provides
    fun provideReturnViewModel(
        returnRepo: ReturnRepo
    ): ReturnViewModel {
        return ReturnViewModel(returnRepo)

    }
}
