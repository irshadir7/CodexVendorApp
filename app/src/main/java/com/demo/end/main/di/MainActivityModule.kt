package com.demo.end.main.di

import com.demo.end.main.network.EndEcommerceApi
import com.demo.end.main.repository.MainRepo
import com.demo.end.main.vm.ProductsListViewModel
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
object MainActivityModule {



    @Singleton
    @Provides
    fun provideMainApi(retrofit: Retrofit.Builder): EndEcommerceApi {
        return retrofit.build()
            .create(EndEcommerceApi::class.java)
    }
    @Singleton
    @Provides
    fun provideMainRepo(
        endEcommerceApi: EndEcommerceApi
    ): MainRepo {
        return MainRepo(endEcommerceApi)
    }

    @Singleton
    @Provides
    fun provideProductListViewModel(
        mainRepo: MainRepo
    ): ProductsListViewModel {
        return ProductsListViewModel(mainRepo)
    }

}