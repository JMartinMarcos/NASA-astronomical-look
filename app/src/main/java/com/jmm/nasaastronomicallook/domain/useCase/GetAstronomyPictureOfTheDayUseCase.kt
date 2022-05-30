package com.jmm.nasaastronomicallook.domain.useCase

import com.jmm.nasaastronomicallook.archetype.Result
import com.jmm.nasaastronomicallook.common.Interactor
import com.jmm.nasaastronomicallook.common.asyncSeq
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository

class GetAstronomyPictureOfTheDayUseCase(
    private val repository: AstronomyPictureOfTheDayRepository
) : Interactor<Unit, AstronomyPictureoftheDay> {

    override suspend operator fun invoke(request: Unit): Result<Exception, AstronomyPictureoftheDay> =
        asyncSeq { repository.getAstronomyPictureOfTheDay() }
}