package com.jmm.nasaastronomicallook.common.di

import com.jmm.nasaastronomicallook.pictureoftheday.AstronomicalPictureDetailActivity
import com.jmm.nasaastronomicallook.pictureoftheday.di.AstronomicalPictureModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

  @ContributesAndroidInjector(modules = [(AstronomicalPictureModule::class)])
  @PerActivity
  internal abstract fun bindAstronomicalPictureDetail(): AstronomicalPictureDetailActivity


}
