package com.jmm.nasaastronomicallook.domain.repository

import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import org.funktionale.either.Either

interface AstronomyPictureOfTheDayRepository {
    fun getAstronomyPictureOfTheDay(): Either<Exception, AstronomyPictureoftheDay>
}