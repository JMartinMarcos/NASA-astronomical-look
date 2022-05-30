package com.jmm.nasaastronomicallook.data.repository

import com.jmm.nasaastronomicallook.archetype.Result
import com.jmm.nasaastronomicallook.archetype.flatMap
import com.jmm.nasaastronomicallook.data.NasaApiClient
import com.jmm.nasaastronomicallook.data.mapper.AstronomyPictureOfTheDayMapper
import com.jmm.nasaastronomicallook.data.mapper.MapperExecutor
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository

class AstronomyPictureOfTheDayRepositoryImpl(
    private val apiClient: NasaApiClient,
    private val astronomyPictureMapper: AstronomyPictureOfTheDayMapper,
    private val mapperExecutor: MapperExecutor
) : AstronomyPictureOfTheDayRepository {

   override suspend fun  getAstronomyPictureOfTheDay(): Result<Exception, AstronomyPictureoftheDay> =
       apiClient.getAstronomyPictureOfTheDay().flatMap { pictureOfTheDay ->
           mapperExecutor { astronomyPictureMapper.mapAstronomyPictureOfTheDay(pictureOfTheDay) }
       }
}
