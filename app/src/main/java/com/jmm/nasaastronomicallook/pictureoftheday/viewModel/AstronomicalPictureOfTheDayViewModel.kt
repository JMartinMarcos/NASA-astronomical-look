package com.jmm.nasaastronomicallook.pictureoftheday.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmm.nasaastronomicallook.domain.AstronomyPictureoftheDay
import com.jmm.nasaastronomicallook.domain.useCase.GetAstronomyPictureOfTheDayUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AstronomicalPictureOfTheDayViewModel(
    private val getAstronomyPictureOfTheDayUseCase: GetAstronomyPictureOfTheDayUseCase
) : ViewModel(), KoinComponent {

    private val _pictureOfTheDay: MutableLiveData<AstronomyPictureoftheDay> = MutableLiveData()
    private var _errorResponse: MutableLiveData<Exception> = MutableLiveData()

    val pictureOfTheDay: LiveData<AstronomyPictureoftheDay> get() = _pictureOfTheDay
    val errorResponse: LiveData<Exception> get() = _errorResponse

    init {
        viewModelScope.launch {
           getAstronomyPictureOfTheDayUseCase(Unit)
               .fold(
               fnL = { _errorResponse.postValue(it) },
               fnR = { _pictureOfTheDay.postValue(it) }
           )
        }
    }

}