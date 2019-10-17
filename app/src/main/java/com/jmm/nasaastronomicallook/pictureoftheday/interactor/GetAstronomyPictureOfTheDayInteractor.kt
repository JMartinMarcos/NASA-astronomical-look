package com.jmm.nasaastronomicallook.pictureoftheday.interactor

import com.jmm.nasaastronomicallook.common.Interactor
import com.jmm.nasaastronomicallook.common.asyncSeq
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay


class GetAstronomyPictureOfTheDayInteractor : Interactor<Unit, AstronomyPictureoftheDay> {

    fun getAstronomyPictureOfTheDay() = asyncSeq {  }
}