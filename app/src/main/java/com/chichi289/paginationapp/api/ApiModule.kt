package com.chichi289.paginationapp.api

import com.google.gson.GsonBuilder
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiModule {

    private fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor(OkHttpProfilerInterceptor())
        return builder.build()
    }

    private fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .client(provideOkHttpClient())
            .baseUrl("https://dummyapi.io/data/v1/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    fun provideDummyApi(): DummyApi =
        provideRetrofit().create(DummyApi::class.java)

    fun provideDummyRepository(): DummyRepository = DummyRepository(provideDummyApi())

}