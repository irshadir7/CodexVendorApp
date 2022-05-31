package com.codex.ventorapp.main.receipt.di

import com.codex.ventorapp.main.receipt.network.ReceiptApi
import com.codex.ventorapp.main.receipt.repository.ReceiptRepo
import com.codex.ventorapp.main.receipt.vm.ReceiptViewModel
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
object ReceiptModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): ReceiptApi {
        return retrofit.build()
            .create(ReceiptApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        receiptApi: ReceiptApi
    ): ReceiptRepo {
        return ReceiptRepo(receiptApi)
    }

    @Singleton
    @Provides
    fun provideSignUpViewModel(
        receiptRepo: ReceiptRepo
    ): ReceiptViewModel {
        return ReceiptViewModel(receiptRepo)

    }
}
