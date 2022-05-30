package com.jmm.nasaastronomicallook.domain.repository

import com.jmm.nasaastronomicallook.archetype.Result
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay

interface AstronomyPictureOfTheDayRepository {
   suspend fun getAstronomyPictureOfTheDay(): Result<Exception, AstronomyPictureoftheDay>
}