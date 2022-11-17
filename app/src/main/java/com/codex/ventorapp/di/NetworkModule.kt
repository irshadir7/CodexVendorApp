package com.codex.ventorapp.di


import android.annotation.SuppressLint
import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.codex.ventorapp.BuildConfig
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * dependency injection module for all the network stuff
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @SuppressLint("StaticFieldLeak")
    lateinit var session: SessionManager
    lateinit var application: Application

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(300, TimeUnit.SECONDS)
        httpClient.readTimeout(300, TimeUnit.SECONDS)
        session = SessionManager(application)

        return Retrofit.Builder()
            .baseUrl(session.getBaseURL())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
    }

}