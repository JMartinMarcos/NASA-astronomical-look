package com.jmm.nasaastronomicallook.pictureoftheday.interactor

import com.jmm.nasaastronomicallook.common.Interactor
import com.jmm.nasaastronomicallook.common.asyncSeq
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository
import org.funktionale.either.Either

class GetAstronomyPictureOfTheDayInteractor (
    private val repository: AstronomyPictureOfTheDayRepository
) : Interactor<Unit, AstronomyPictureoftheDay>() {

    override suspend fun execute(request: Unit): Either<Throwable, AstronomyPictureoftheDay> =
        asyncSeq { repository.getAstronomyPictureOfTheDay() }
}