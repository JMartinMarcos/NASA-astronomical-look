package com.jmm.nasaastronomicallook.data

import com.jmm.nasaastronomicallook.data.entity.AstronomyPictureoftheDayEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NasaService {

    @Headers("Content-Type: application/json")
    @GET("planetary/apod")
    fun getAPOD(
        @Query("date") date: String,
        @Query("hd") hd: Boolean,
        @Query("api_key") apiKey: String
    ): Call<AstronomyPictureoftheDayEntity>

}