package com.jmm.nasaastronomicallook.pictureoftheday.di

import com.jmm.nasaastronomicallook.data.mapper.AstronomyPictureOfTheDayMapper
import com.jmm.nasaastronomicallook.data.repository.AstronomyPictureOfTheDayRepositoryImpl
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository
import com.jmm.nasaastronomicallook.pictureoftheday.AstronomicalPictureDetailActivity
import com.jmm.nasaastronomicallook.domain.useCase.GetAstronomyPictureOfTheDayUseCase
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val pictureOfTheDayModule = module {

    scope(named<AstronomicalPictureDetailActivity>()) {
        scoped <AstronomyPictureOfTheDayRepository> { AstronomyPictureOfTheDayRepositoryImpl(get(),get(),get()) }
        scoped { GetAstronomyPictureOfTheDayUseCase(get())  }
        scoped { AstronomyPictureOfTheDayMapper() }
        viewModel { AstronomicalPictureOfTheDayViewModel(get()) }
    }
}