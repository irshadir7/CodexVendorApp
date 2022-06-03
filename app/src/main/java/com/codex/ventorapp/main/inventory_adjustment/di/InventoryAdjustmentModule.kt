package com.codex.ventorapp.main.inventory_adjustment.di

import com.codex.ventorapp.main.inventory_adjustment.network.InventoryAdjustmentApi
import com.codex.ventorapp.main.inventory_adjustment.repository.AdjustmentRepo
import com.codex.ventorapp.main.inventory_adjustment.vm.InventoryAdjustmentViewModel
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
object InventoryAdjustmentModule {
    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): InventoryAdjustmentApi {
        return retrofit.build()
            .create(InventoryAdjustmentApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMainRepo(
        inventoryAdjustmentApi: InventoryAdjustmentApi
    ): AdjustmentRepo {
        return AdjustmentRepo(inventoryAdjustmentApi)
    }

    @Singleton
    @Provides
    fun provideInventoryAdjustmentViewModel(
        adjustmentRepo: AdjustmentRepo
    ): InventoryAdjustmentViewModel {
        return InventoryAdjustmentViewModel(adjustmentRepo)
    }
}