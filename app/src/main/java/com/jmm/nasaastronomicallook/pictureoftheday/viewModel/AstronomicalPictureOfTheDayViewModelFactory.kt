package com.jmm.nasaastronomicallook.pictureoftheday.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jmm.nasaastronomicallook.pictureoftheday.interactor.GetAstronomyPictureOfTheDayInteractor


class AstronomicalPictureOfTheDayViewModelFactory (
    private val getAstronomyPictureOfTheDayInteractor: GetAstronomyPictureOfTheDayInteractor
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AstronomicalPictureOfTheDayViewModel(getAstronomyPictureOfTheDayInteractor) as T
    }
}