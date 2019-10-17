package com.jmm.nasaastronomicallook.data.repository

import com.jmm.nasaastronomicallook.data.NasaApiClient
import com.jmm.nasaastronomicallook.data.mapper.AstronomyPictureOfTheDayMapper
import com.jmm.nasaastronomicallook.data.mapper.MapperExecutor
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository
import org.funktionale.either.Either

class AstronomyPictureOfTheDayRepositoryImpl : AstronomyPictureOfTheDayRepository {

    private val apiClient = NasaApiClient()
    private val astronomyPictureMapper = AstronomyPictureOfTheDayMapper()
    private val mapperExecutor = MapperExecutor()

    override fun getAstronomyPictureOfTheDay(): Either<Exception, AstronomyPictureoftheDay> {
        val response = apiClient.getAstronomyPictureOfTheDay()
        return if (response.isRight()) {
            mapperExecutor { astronomyPictureMapper.mapAstronomyPictureOfTheDay(response.right().get()) }
        } else {
            Either.left(response.left().get())
        }
    }
}
