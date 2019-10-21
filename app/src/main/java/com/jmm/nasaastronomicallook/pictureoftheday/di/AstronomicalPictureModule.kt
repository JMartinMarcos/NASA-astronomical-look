package com.jmm.nasaastronomicallook.pictureoftheday.di

import com.jmm.nasaastronomicallook.common.di.PerActivity
import com.jmm.nasaastronomicallook.data.NasaApiClient
import com.jmm.nasaastronomicallook.data.mapper.AstronomyPictureOfTheDayMapper
import com.jmm.nasaastronomicallook.data.mapper.MapperExecutor
import com.jmm.nasaastronomicallook.data.repository.AstronomyPictureOfTheDayRepositoryImpl
import com.jmm.nasaastronomicallook.domain.repository.AstronomyPictureOfTheDayRepository
import com.jmm.nasaastronomicallook.pictureoftheday.interactor.GetAstronomyPictureOfTheDayInteractor
import com.jmm.nasaastronomicallook.pictureoftheday.viewModel.AstronomicalPictureOfTheDayViewModel
import dagger.Module
import dagger.Provides

@Module
class AstronomicalPictureModule {

    @Provides
    @PerActivity
    fun provideAstronomyPictureOfTheDayRepository(
        apiClient: NasaApiClient,
        astronomyPictureMapper: AstronomyPictureOfTheDayMapper,
        mapperExecutor: MapperExecutor
    ): AstronomyPictureOfTheDayRepository =
        AstronomyPictureOfTheDayRepositoryImpl(apiClient, astronomyPictureMapper, mapperExecutor)

    @Provides
    @PerActivity
    fun provideGetAstronomyPictureOfTheDayInteractor(astronomyPictureOfTheDayRepository: AstronomyPictureOfTheDayRepository): GetAstronomyPictureOfTheDayInteractor =
        GetAstronomyPictureOfTheDayInteractor(astronomyPictureOfTheDayRepository)


    @Provides
    @PerActivity
    fun provideAstronomicalPictureOfTheDayViewModel(getAstronomyPictureOfTheDayInteractor: GetAstronomyPictureOfTheDayInteractor): AstronomicalPictureOfTheDayViewModel =
        AstronomicalPictureOfTheDayViewModel(getAstronomyPictureOfTheDayInteractor)


    @Provides
    @PerActivity
    fun provideAstronomyPictureOfTheDayMapper() = AstronomyPictureOfTheDayMapper()

}
