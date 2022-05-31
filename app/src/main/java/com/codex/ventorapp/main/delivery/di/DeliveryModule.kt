package com.codex.ventorapp.main.delivery.di

import com.codex.ventorapp.main.delivery.network.DeliveryApi
import com.codex.ventorapp.main.delivery.repository.DeliveryRepo
import com.codex.ventorapp.main.delivery.vm.DeliveryViewModel
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
object DeliveryModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): DeliveryApi {
        return retrofit.build()
            .create(DeliveryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        deliveryApi: DeliveryApi
    ): DeliveryRepo {
        return DeliveryRepo(deliveryApi)
    }

    @Singleton
    @Provides
    fun provideDeliveryViewModel(
        deliveryRepo: DeliveryRepo
    ): DeliveryViewModel {
        return DeliveryViewModel(deliveryRepo)

    }
}
