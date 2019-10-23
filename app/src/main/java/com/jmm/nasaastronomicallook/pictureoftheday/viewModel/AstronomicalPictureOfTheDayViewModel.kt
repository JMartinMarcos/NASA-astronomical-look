package com.jmm.nasaastronomicallook.pictureoftheday.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.pictureoftheday.interactor.GetAstronomyPictureOfTheDayInteractor
import kotlinx.coroutines.launch

class AstronomicalPictureOfTheDayViewModel constructor(
    private val getAstronomyPictureOfTheDayInteractor: GetAstronomyPictureOfTheDayInteractor
) : ViewModel() {

    private val _pictureOfTheDay: MutableLiveData<AstronomyPictureoftheDay> = MutableLiveData()
    private var _errorResponse: MutableLiveData<Throwable?> = MutableLiveData()

    val pictureOfTheDay: LiveData<AstronomyPictureoftheDay> get() = _pictureOfTheDay
    val errorResponse: LiveData<Throwable?> get() = _errorResponse

    init {
        viewModelScope.launch {
           val (error, response) = getAstronomyPictureOfTheDayInteractor(Unit)
            _pictureOfTheDay.value = response
            error?.let { _errorResponse.value = it }
        }
    }

}