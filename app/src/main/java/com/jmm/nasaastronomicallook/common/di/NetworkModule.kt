package com.jmm.nasaastronomicallook.common.di

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jmm.nasaastronomicallook.BuildConfig
import com.jmm.nasaastronomicallook.data.NasaApiClient
import com.jmm.nasaastronomicallook.data.NasaService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val API_KEY = "api_key"

@Module
class NetworkModule {

    @Provides
    @Named(API_KEY)
    @Singleton
    fun provideApiKey(context: Context): String = BuildConfig.API_KEY

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()

    @Provides
    @Singleton
    fun provideOkHttpClientBuilder(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(8000, TimeUnit.MILLISECONDS)
            .readTimeout(8000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(httpLoggingInterceptor)


    @Provides
    @Singleton
    fun provideNasaService(retrofit: Retrofit): NasaService = retrofit.create(NasaService::class.java)


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClientBuilder: OkHttpClient.Builder, gson: Gson): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClientBuilder.build())
            .build()

    @Provides
    @Singleton
    fun provideOkHttpLogginClient(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideNasaApiClient(nasaService: NasaService, gson: Gson, @Named(API_KEY) apiKey: String) : NasaApiClient = NasaApiClient(nasaService, gson, apiKey)

}