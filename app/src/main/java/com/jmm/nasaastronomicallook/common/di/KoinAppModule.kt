package com.jmm.nasaastronomicallook.common.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.jmm.nasaastronomicallook.BuildConfig
import com.jmm.nasaastronomicallook.data.NasaApiClient
import com.jmm.nasaastronomicallook.data.NasaService
import com.jmm.nasaastronomicallook.data.mapper.MapperExecutor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val TIME_OUT_MILLIS = 8000L

val appModule = module {
    single { MapperExecutor() }
}

val networkModule = module {

    val apiKey = BuildConfig.API_KEY

    val gSon = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val okHttpClientBuilder = OkHttpClient.Builder()
        .connectTimeout(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)
        .readTimeout(TIME_OUT_MILLIS, TimeUnit.MILLISECONDS)
        .addNetworkInterceptor(httpLoggingInterceptor)

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create(gSon))
        .client(okHttpClientBuilder.build())
        .build()

    val nasaService = retrofit.create(NasaService::class.java)

    val nasaApiClient = NasaApiClient(nasaService, gSon, apiKey)

    single { nasaApiClient }
}

